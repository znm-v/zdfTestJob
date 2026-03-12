package com.sweetbite.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 添加到购物车DTO
 */
@Data
public class AddToCartDTO {
    
    @NotNull(message = "商品类型不能为空")
    private Integer itemType;  // 1-食物 2-礼品
    
    @NotNull(message = "商品ID不能为空")
    private Long itemId;
    
    private Integer quantity = 1;
}
