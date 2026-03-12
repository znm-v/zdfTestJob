package com.sweetbite.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sweetbite.common.PageResult;
import com.sweetbite.dto.CreateOrderDTO;
import com.sweetbite.entity.Order;
import com.sweetbite.vo.OrderVO;

/**
 * 订单服务接口
 */
public interface IOrderService extends IService<Order> {
    
    /**
     * 创建订单
     */
    OrderVO createOrder(Long userId, CreateOrderDTO dto);
    
    /**
     * 获取订单列表
     */
    PageResult<OrderVO> listOrders(Long coupleAccountId, Integer status, Long current, Long size);
    
    /**
     * 获取订单详情
     */
    OrderVO getOrderDetail(Long orderId);
    
    /**
     * 取消订单
     */
    void cancelOrder(Long orderId, Long userId);
}
