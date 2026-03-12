package com.sweetbite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sweetbite.common.Constants;
import com.sweetbite.common.ErrorCode;
import com.sweetbite.common.PageResult;
import com.sweetbite.dto.CreateOrderDTO;
import com.sweetbite.entity.*;
import com.sweetbite.exception.BusinessException;
import com.sweetbite.mapper.*;
import com.sweetbite.service.ICoupleAccountService;
import com.sweetbite.service.IOrderService;
import com.sweetbite.vo.OrderVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单服务实现
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final UserMapper userMapper;
    private final FoodMapper foodMapper;
    private final GiftMapper giftMapper;
    private final ICoupleAccountService coupleAccountService;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderVO createOrder(Long userId, CreateOrderDTO dto) {
        // 获取用户信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        
        // 计算订单总价
        int totalPoints = 0;
        List<OrderItem> orderItems = new ArrayList<>();
        
        for (CreateOrderDTO.OrderItemDTO itemDTO : dto.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setItemType(itemDTO.getItemType());
            orderItem.setItemId(itemDTO.getItemId());
            orderItem.setQuantity(itemDTO.getQuantity());
            
            // 根据类型获取商品信息
            if (itemDTO.getItemType().equals(Constants.ITEM_TYPE_FOOD)) {
                Food food = foodMapper.selectById(itemDTO.getItemId());
                if (food == null) {
                    throw new BusinessException(ErrorCode.FOOD_NOT_FOUND);
                }
                orderItem.setItemName(food.getName());
                orderItem.setItemImage(food.getImageUrl());
                orderItem.setPoints(food.getPoints());
            } else if (itemDTO.getItemType().equals(Constants.ITEM_TYPE_GIFT)) {
                Gift gift = giftMapper.selectById(itemDTO.getItemId());
                if (gift == null) {
                    throw new BusinessException(ErrorCode.GIFT_NOT_FOUND);
                }
                orderItem.setItemName(gift.getName());
                orderItem.setItemImage(gift.getImageUrl());
                orderItem.setPoints(gift.getPoints());
            }
            
            orderItem.setTotalPoints(orderItem.getPoints() * orderItem.getQuantity());
            totalPoints += orderItem.getTotalPoints();
            orderItems.add(orderItem);
        }
        
        // 生成订单号
        String orderNo = generateOrderNo(dto.getOrderType());
        
        // 创建订单
        Order order = Order.builder()
                .orderNo(orderNo)
                .coupleAccountId(user.getCoupleAccountId())
                .userId(userId)
                .orderType(dto.getOrderType())
                .totalPoints(totalPoints)
                .itemCount(orderItems.stream().mapToInt(OrderItem::getQuantity).sum())
                .status(Constants.ORDER_STATUS_PAID)
                .remark(dto.getRemark())
                .payTime(LocalDateTime.now())
                .build();
        orderMapper.insert(order);
        
        // 创建订单明细
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
            orderItemMapper.insert(item);
        }
        
        // 扣除积分
        coupleAccountService.deductPoints(user.getCoupleAccountId(), totalPoints, 
                "订单消费: " + orderNo);
        
        // 更新商品销量
        for (OrderItem item : orderItems) {
            if (item.getItemType().equals(Constants.ITEM_TYPE_FOOD)) {
                Food food = foodMapper.selectById(item.getItemId());
                food.setSales(food.getSales() + item.getQuantity());
                foodMapper.updateById(food);
            } else if (item.getItemType().equals(Constants.ITEM_TYPE_GIFT)) {
                Gift gift = giftMapper.selectById(item.getItemId());
                gift.setSales(gift.getSales() + item.getQuantity());
                giftMapper.updateById(gift);
            }
        }
        
        // 构建返回结果
        return buildOrderVO(order, orderItems);
    }
    
    @Override
    public PageResult<OrderVO> listOrders(Long coupleAccountId, Integer status, Long current, Long size) {
        Page<Order> page = new Page<>(current, size);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>()
                .eq(Order::getCoupleAccountId, coupleAccountId)
                .orderByDesc(Order::getCreateTime);
        
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        
        IPage<Order> result = orderMapper.selectPage(page, wrapper);
        
        List<OrderVO> orderVOs = result.getRecords().stream().map(order -> {
            List<OrderItem> items = orderItemMapper.selectList(
                    new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, order.getId()));
            return buildOrderVO(order, items);
        }).collect(Collectors.toList());
        
        return new PageResult<>(orderVOs, result.getTotal(), result.getCurrent(), result.getSize());
    }
    
    @Override
    public OrderVO getOrderDetail(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        
        List<OrderItem> items = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, orderId));
        
        return buildOrderVO(order, items);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long orderId, Long userId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        
        if (!order.getStatus().equals(Constants.ORDER_STATUS_PAID)) {
            throw new BusinessException(ErrorCode.ORDER_STATUS_ERROR);
        }
        
        // 更新订单状态
        order.setStatus(Constants.ORDER_STATUS_CANCELLED);
        orderMapper.updateById(order);
        
        // 退还积分
        User user = userMapper.selectById(userId);
        coupleAccountService.addPoints(user.getCoupleAccountId(), order.getTotalPoints(), 
                "订单取消退款: " + order.getOrderNo());
    }
    
    /**
     * 生成订单号
     */
    private String generateOrderNo(Integer orderType) {
        String prefix = orderType.equals(Constants.ORDER_TYPE_FOOD) ? "OD" : "GF";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return prefix + timestamp + (int)(Math.random() * 1000);
    }
    
    /**
     * 构建订单VO
     */
    private OrderVO buildOrderVO(Order order, List<OrderItem> items) {
        List<OrderVO.OrderItemVO> itemVOs = items.stream().map(item -> 
                OrderVO.OrderItemVO.builder()
                        .itemName(item.getItemName())
                        .itemImage(item.getItemImage())
                        .points(item.getPoints())
                        .quantity(item.getQuantity())
                        .totalPoints(item.getTotalPoints())
                        .build()
        ).collect(Collectors.toList());
        
        return OrderVO.builder()
                .id(order.getId())
                .orderNo(order.getOrderNo())
                .orderType(order.getOrderType())
                .totalPoints(order.getTotalPoints())
                .itemCount(order.getItemCount())
                .status(order.getStatus())
                .remark(order.getRemark())
                .payTime(order.getPayTime())
                .createTime(order.getCreateTime())
                .items(itemVOs)
                .build();
    }
}
