package com.sweetbite.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 注册请求DTO
 */
@Data
public class RegisterDTO {
    
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    private String password;
    
    @NotBlank(message = "昵称不能为空")
    private String nickname;
    
    private String phone;
    
    @NotBlank(message = "情侣名称不能为空")
    private String coupleName;
}
