package com.sweetbite.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单响应VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO {
    private Long id;
    private String orderNo;
    private Integer orderType;
    private Integer totalPoints;
    private Integer itemCount;
    private Integer status;
    private String remark;
    private LocalDateTime payTime;
    private LocalDateTime createTime;
    private List<OrderItemVO> items;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemVO {
        private String itemName;
        private String itemImage;
        private Integer points;
        private Integer quantity;
        private Integer totalPoints;
    }
}
