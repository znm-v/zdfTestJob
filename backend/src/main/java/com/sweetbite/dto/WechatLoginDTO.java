package com.sweetbite.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 微信登录请求DTO
 */
@Data
public class WechatLoginDTO {
    
    @NotBlank(message = "code不能为空")
    private String code;
    
    private String nickName;
    
    private String avatarUrl;
    
    private String gender;
    
    private String province;
    
    private String city;
    
    private String country;
}
