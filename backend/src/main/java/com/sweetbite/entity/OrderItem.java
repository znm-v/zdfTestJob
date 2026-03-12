package com.sweetbite.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 订单明细实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("order_item")
public class OrderItem implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long orderId;
    
    private Integer itemType;
    
    private Long itemId;
    
    private String itemName;
    
    private String itemImage;
    
    private Integer points;
    
    private Integer quantity;
    
    private Integer totalPoints;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
