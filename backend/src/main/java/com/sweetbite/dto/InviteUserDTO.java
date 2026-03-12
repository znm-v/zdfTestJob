package com.sweetbite.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 邀请用户请求DTO
 */
@Data
public class InviteUserDTO {
    
    @NotBlank(message = "邀请码不能为空")
    private String inviteCode;
    
    private String nickName;
    
    private String avatarUrl;
}
