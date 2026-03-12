package com.sweetbite.controller;

import com.sweetbite.common.Result;
import com.sweetbite.dto.AddToCartDTO;
import com.sweetbite.service.ICartService;
import com.sweetbite.utils.JwtUtil;
import com.sweetbite.vo.CartItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 购物车控制器
 */
@Api(tags = "购物车接口")
@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    
    private final ICartService cartService;
    
    @ApiOperation("添加到购物车")
    @PostMapping("/add")
    public Result<CartItemVO> addToCart(HttpServletRequest request,
                                        @Validated @RequestBody AddToCartDTO dto) {
        Long userId = getUserIdFromRequest(request);
        CartItemVO cartItem = cartService.addToCart(userId, dto);
        return Result.success("添加成功", cartItem);
    }
    
    @ApiOperation("获取购物车列表")
    @GetMapping("/list")
    public Result<List<CartItemVO>> getCartList(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        List<CartItemVO> cartList = cartService.getCartList(userId);
        return Result.success(cartList);
    }
    
    @ApiOperation("删除购物车项")
    @DeleteMapping("/{id}")
    public Result<String> removeCartItem(HttpServletRequest request, @PathVariable Long id) {
        Long userId = getUserIdFromRequest(request);
        cartService.removeCartItem(userId, id);
        return Result.success("删除成功", "success");
    }
    
    @ApiOperation("清空购物车")
    @DeleteMapping("/clear")
    public Result<String> clearCart(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        cartService.clearCart(userId);
        return Result.success("清空成功", "success");
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
