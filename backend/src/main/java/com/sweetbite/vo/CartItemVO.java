package com.sweetbite.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 购物车项响应VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemVO {
    private Long id;
    private Long itemId;
    private Integer itemType;
    private String itemName;
    private String itemImage;
    private Integer points;
    private Integer quantity;
    private String category;
    private String description;
}
