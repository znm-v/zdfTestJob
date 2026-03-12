package com.sweetbite.dto;

import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 创建订单请求DTO
 */
@Data
public class CreateOrderDTO {
    
    @NotNull(message = "订单类型不能为空")
    private Integer orderType;
    
    @NotEmpty(message = "订单项不能为空")
    private List<OrderItemDTO> items;
    
    private String remark;
    
    @Data
    public static class OrderItemDTO {
        @NotNull(message = "商品类型不能为空")
        private Integer itemType;
        
        @NotNull(message = "商品ID不能为空")
        private Long itemId;
        
        @NotNull(message = "数量不能为空")
        private Integer quantity;
    }
}
