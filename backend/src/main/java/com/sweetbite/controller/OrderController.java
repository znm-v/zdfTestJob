package com.sweetbite.controller;

import com.sweetbite.common.PageResult;
import com.sweetbite.common.Result;
import com.sweetbite.dto.CreateOrderDTO;
import com.sweetbite.entity.User;
import com.sweetbite.mapper.UserMapper;
import com.sweetbite.service.IOrderService;
import com.sweetbite.utils.JwtUtil;
import com.sweetbite.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 订单控制器
 */
@Api(tags = "订单接口")
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    
    private final IOrderService orderService;
    private final UserMapper userMapper;
    
    @ApiOperation("创建订单")
    @PostMapping("/create")
    public Result<OrderVO> createOrder(HttpServletRequest request,
                                       @Validated @RequestBody CreateOrderDTO dto) {
        Long userId = getUserIdFromRequest(request);
        OrderVO orderVO = orderService.createOrder(userId, dto);
        return Result.success("下单成功", orderVO);
    }
    
    @ApiOperation("获取订单列表")
    @GetMapping("/list")
    public Result<PageResult<OrderVO>> listOrders(
            HttpServletRequest request,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long size) {
        Long userId = getUserIdFromRequest(request);
        User user = userMapper.selectById(userId);
        PageResult<OrderVO> result = orderService.listOrders(
                user.getCoupleAccountId(), status, page, size);
        return Result.success(result);
    }
    
    @ApiOperation("获取订单详情")
    @GetMapping("/{id}")
    public Result<OrderVO> getOrderDetail(@PathVariable Long id) {
        OrderVO orderVO = orderService.getOrderDetail(id);
        return Result.success(orderVO);
    }
    
    @ApiOperation("取消订单")
    @PutMapping("/cancel/{id}")
    public Result<String> cancelOrder(HttpServletRequest request, @PathVariable Long id) {
        Long userId = getUserIdFromRequest(request);
        orderService.cancelOrder(id, userId);
        return Result.success("取消成功");
    }
    
    private Long getUserIdFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return JwtUtil.getUserIdFromToken(token);
        }
        return null;
    }
}
