package com.sweetbite.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息响应VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVO {
    private Long id;
    private String username;
    private String nickname;
    private String avatarUrl;
    private String role;
    private Long coupleAccountId;
    private Integer personalPoints;
    private CoupleAccountVO coupleAccount;
}
