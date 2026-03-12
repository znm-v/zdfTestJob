package com.sweetbite.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sweetbite.dto.AddToCartDTO;
import com.sweetbite.entity.CartItem;
import com.sweetbite.vo.CartItemVO;

import java.util.List;

/**
 * 购物车服务接口
 */
public interface ICartService extends IService<CartItem> {
    
    /**
     * 添加到购物车
     */
    CartItemVO addToCart(Long userId, AddToCartDTO dto);
    
    /**
     * 获取购物车列表
     */
    List<CartItemVO> getCartList(Long userId);
    
    /**
     * 删除购物车项
     */
    void removeCartItem(Long userId, Long cartItemId);
    
    /**
     * 清空购物车
     */
    void clearCart(Long userId);
}
