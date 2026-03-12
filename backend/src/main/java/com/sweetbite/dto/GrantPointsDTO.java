package com.sweetbite.dto;

import lombok.Data;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 发放积分请求DTO
 */
@Data
public class GrantPointsDTO {
    
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    @NotNull(message = "积分不能为空")
    @Min(value = 1, message = "积分必须大于0")
    private Integer points;
    
    private String remark;
}
