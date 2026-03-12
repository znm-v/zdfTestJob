package com.sweetbite.dto;

import lombok.Data;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 创建食物请求DTO
 */
@Data
public class CreateFoodDTO {
    
    @NotBlank(message = "食物名称不能为空")
    private String name;
    
    private String description;
    
    private String imageUrl;
    
    @NotBlank(message = "分类不能为空")
    private String category;
    
    @NotNull(message = "积分不能为空")
    @Min(value = 1, message = "积分必须大于0")
    private Integer points;
    
    private Integer stock = -1;  // 默认无限库存
}
