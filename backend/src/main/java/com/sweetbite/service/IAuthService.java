package com.sweetbite.service;

import com.sweetbite.dto.LoginDTO;
import com.sweetbite.dto.RegisterDTO;
import com.sweetbite.dto.WechatLoginDTO;
import com.sweetbite.dto.InviteUserDTO;
import com.sweetbite.vo.LoginVO;
import com.sweetbite.vo.UserInfoVO;

/**
 * 认证服务接口
 */
public interface IAuthService {
    
    /**
     * 用户登录
     */
    LoginVO login(LoginDTO dto);
    
    /**
     * 用户注册
     */
    void register(RegisterDTO dto);
    
    /**
     * 获取用户信息
     */
    UserInfoVO getUserInfo(Long userId);
    
    /**
     * 微信登录
     */
    LoginVO wechatLogin(WechatLoginDTO dto);
    
    /**
     * 生成邀请码
     */
    String generateInviteCode(Long coupleAccountId);
    
    /**
     * 邀请用户加入
     */
    LoginVO inviteUser(InviteUserDTO dto);

    /**
     * 更新用户信息
     */
    void updateUserInfo(Long userId, String nickname, String avatarUrl);
}
