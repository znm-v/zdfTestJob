package com.sweetbite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sweetbite.common.ErrorCode;
import com.sweetbite.dto.AddToCartDTO;
import com.sweetbite.entity.CartItem;
import com.sweetbite.entity.Food;
import com.sweetbite.entity.Gift;
import com.sweetbite.exception.BusinessException;
import com.sweetbite.mapper.CartItemMapper;
import com.sweetbite.mapper.FoodMapper;
import com.sweetbite.mapper.GiftMapper;
import com.sweetbite.service.ICartService;
import com.sweetbite.vo.CartItemVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车服务实现
 */
@Service
@RequiredArgsConstructor
public class CartServiceImpl extends ServiceImpl<CartItemMapper, CartItem> implements ICartService {
    
    private final CartItemMapper cartItemMapper;
    private final FoodMapper foodMapper;
    private final GiftMapper giftMapper;
    
    @Override
    public CartItemVO addToCart(Long userId, AddToCartDTO dto) {
        // 检查商品是否存在
        if (dto.getItemType() == 1) {
            Food food = foodMapper.selectById(dto.getItemId());
            if (food == null) {
                throw new BusinessException(ErrorCode.FOOD_NOT_FOUND);
            }
        } else if (dto.getItemType() == 2) {
            Gift gift = giftMapper.selectById(dto.getItemId());
            if (gift == null) {
                throw new BusinessException(ErrorCode.GIFT_NOT_FOUND);
            }
        }
        
        // 检查购物车中是否已存在该商品
        CartItem existingItem = cartItemMapper.selectOne(
            new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getUserId, userId)
                .eq(CartItem::getItemType, dto.getItemType())
                .eq(CartItem::getItemId, dto.getItemId())
        );
        
        if (existingItem != null) {
            // 如果已存在，增加数量
            existingItem.setQuantity(existingItem.getQuantity() + dto.getQuantity());
            cartItemMapper.updateById(existingItem);
            return buildCartItemVO(existingItem);
        } else {
            // 创建新的购物车项
            CartItem cartItem = CartItem.builder()
                .userId(userId)
                .itemType(dto.getItemType())
                .itemId(dto.getItemId())
                .quantity(dto.getQuantity())
                .build();
            cartItemMapper.insert(cartItem);
            return buildCartItemVO(cartItem);
        }
    }
    
    @Override
    public List<CartItemVO> getCartList(Long userId) {
        List<CartItem> cartItems = cartItemMapper.selectList(
            new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getUserId, userId)
                .orderByDesc(CartItem::getCreateTime)
        );
        
        List<CartItemVO> result = new ArrayList<>();
        for (CartItem item : cartItems) {
            result.add(buildCartItemVO(item));
        }
        return result;
    }
    
    @Override
    public void removeCartItem(Long userId, Long cartItemId) {
        CartItem cartItem = cartItemMapper.selectById(cartItemId);
        if (cartItem == null || !cartItem.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.CART_ITEM_NOT_FOUND);
        }
        cartItemMapper.deleteById(cartItemId);
    }
    
    @Override
    public void clearCart(Long userId) {
        cartItemMapper.delete(
            new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getUserId, userId)
        );
    }
    
    private CartItemVO buildCartItemVO(CartItem cartItem) {
        CartItemVO vo = new CartItemVO();
        vo.setId(cartItem.getId());
        vo.setItemId(cartItem.getItemId());
        vo.setItemType(cartItem.getItemType());
        vo.setQuantity(cartItem.getQuantity());
        
        if (cartItem.getItemType() == 1) {
            Food food = foodMapper.selectById(cartItem.getItemId());
            if (food != null) {
                vo.setItemName(food.getName());
                vo.setItemImage(food.getImageUrl());
                vo.setPoints(food.getPoints());
                vo.setCategory(food.getCategory());
                vo.setDescription(food.getDescription());
            }
        } else if (cartItem.getItemType() == 2) {
            Gift gift = giftMapper.selectById(cartItem.getItemId());
            if (gift != null) {
                vo.setItemName(gift.getName());
                vo.setItemImage(gift.getImageUrl());
                vo.setPoints(gift.getPoints());
                vo.setDescription(gift.getDescription());
            }
        }
        
        return vo;
    }
}
