package com.sweetbite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sweetbite.common.Constants;
import com.sweetbite.common.ErrorCode;
import com.sweetbite.dto.LoginDTO;
import com.sweetbite.dto.RegisterDTO;
import com.sweetbite.dto.WechatLoginDTO;
import com.sweetbite.dto.InviteUserDTO;
import com.sweetbite.entity.CoupleAccount;
import com.sweetbite.entity.User;
import com.sweetbite.entity.InviteCode;
import com.sweetbite.exception.BusinessException;
import com.sweetbite.mapper.CoupleAccountMapper;
import com.sweetbite.mapper.UserMapper;
import com.sweetbite.mapper.InviteCodeMapper;
import com.sweetbite.service.IAuthService;
import com.sweetbite.service.ICoupleAccountService;
import com.sweetbite.utils.JwtUtil;
import com.sweetbite.utils.PasswordUtil;
import com.sweetbite.utils.WechatUtil;
import com.sweetbite.vo.CoupleAccountVO;
import com.sweetbite.vo.LoginVO;
import com.sweetbite.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

/**
 * 认证服务实现
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    
    private final UserMapper userMapper;
    private final CoupleAccountMapper coupleAccountMapper;
    private final InviteCodeMapper inviteCodeMapper;
    private final ICoupleAccountService coupleAccountService;
    private final WechatUtil wechatUtil;
    
    @Override
    public LoginVO login(LoginDTO dto) {
        // 查询用户
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, dto.getUsername()));
        
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        
        // 验证密码
        if (!PasswordUtil.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_ERROR);
        }
        
        // 生成Token
        String token = JwtUtil.generateToken(user.getId(), user.getUsername());
        
        // 构建用户信息
        UserInfoVO userInfo = UserInfoVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole())
                .coupleAccountId(user.getCoupleAccountId())
                .personalPoints(user.getPersonalPoints())
                .build();
        
        return LoginVO.builder()
                .token(token)
                .userInfo(userInfo)
                .build();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterDTO dto) {
        // 检查用户名是否存在
        Long count = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, dto.getUsername()));
        
        if (count > 0) {
            throw new BusinessException(ErrorCode.USERNAME_EXISTS);
        }
        
        // 创建情侣账户
        CoupleAccount coupleAccount = CoupleAccount.builder()
                .coupleName(dto.getCoupleName())
                .togetherDate(LocalDate.now())
                .totalPoints(0)
                .usedPoints(0)
                .availablePoints(0)
                .build();
        coupleAccountMapper.insert(coupleAccount);
        
        // 创建用户
        User user = User.builder()
                .coupleAccountId(coupleAccount.getId())
                .username(dto.getUsername())
                .password(PasswordUtil.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .phone(dto.getPhone())
                .role(Constants.ROLE_ADMIN)  // 注册用户默认为管理员
                .build();
        userMapper.insert(user);
    }
    
    @Override
    public UserInfoVO getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        
        UserInfoVO userInfo = UserInfoVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole())
                .coupleAccountId(user.getCoupleAccountId())
                .personalPoints(user.getPersonalPoints())
                .build();
        
        // 获取情侣账户信息
        if (user.getCoupleAccountId() != null) {
            CoupleAccountVO coupleAccount = coupleAccountService.getCoupleAccountInfo(user.getCoupleAccountId());
            userInfo.setCoupleAccount(coupleAccount);
        }
        
        return userInfo;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVO wechatLogin(WechatLoginDTO dto) {
        // 调用微信 API 验证 code，获取 openid
        String openid = wechatUtil.getOpenid(dto.getCode());
        
        if (openid == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        
        // 查询是否已存在该微信用户
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, "wechat_" + openid));
        
        if (user == null) {
            // 新用户需要邀请码才能创建账户
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        
        // 生成Token
        String token = JwtUtil.generateToken(user.getId(), user.getUsername());
        
        // 构建用户信息
        UserInfoVO userInfo = UserInfoVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole())
                .coupleAccountId(user.getCoupleAccountId())
                .personalPoints(user.getPersonalPoints())
                .build();
        
        return LoginVO.builder()
                .token(token)
                .userInfo(userInfo)
                .build();
    }
    
    @Override
    public String generateInviteCode(Long coupleAccountId) {
        // 生成唯一邀请码
        String code = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        InviteCode inviteCode = InviteCode.builder()
                .coupleAccountId(coupleAccountId)
                .code(code)
                .status(0)  // 未使用
                .build();
        
        inviteCodeMapper.insert(inviteCode);
        return code;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVO inviteUser(InviteUserDTO dto) {
        // 查询邀请码
        InviteCode inviteCode = inviteCodeMapper.selectOne(new LambdaQueryWrapper<InviteCode>()
                .eq(InviteCode::getCode, dto.getInviteCode())
                .eq(InviteCode::getStatus, 0));
        
        if (inviteCode == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND);  // 邀请码无效或已使用
        }
        
        // 获取情侣账户
        CoupleAccount coupleAccount = coupleAccountMapper.selectById(inviteCode.getCoupleAccountId());
        if (coupleAccount == null) {
            throw new BusinessException(ErrorCode.COUPLE_ACCOUNT_NOT_FOUND);
        }
        
        // 创建新用户（被邀请者）
        String openid = UUID.randomUUID().toString().substring(0, 8);
        String username = "user_" + openid;
        String defaultPassword = "123456";  // 默认密码
        
        User newUser = User.builder()
                .coupleAccountId(coupleAccount.getId())
                .username(username)
                .password(PasswordUtil.encode(defaultPassword))
                .nickname(dto.getNickName())
                .avatarUrl(dto.getAvatarUrl())
                .role(Constants.ROLE_USER)  // 被邀请者为普通用户
                .personalPoints(0)  // 初始积分为0
                .build();
        userMapper.insert(newUser);
        
        // 标记邀请码已使用
        inviteCode.setStatus(1);
        inviteCode.setUsedByUserId(newUser.getId());
        inviteCodeMapper.updateById(inviteCode);
        
        // 生成Token
        String token = JwtUtil.generateToken(newUser.getId(), newUser.getUsername());
        
        // 构建用户信息
        UserInfoVO userInfo = UserInfoVO.builder()
                .id(newUser.getId())
                .username(newUser.getUsername())
                .nickname(newUser.getNickname())
                .avatarUrl(newUser.getAvatarUrl())
                .role(newUser.getRole())
                .coupleAccountId(newUser.getCoupleAccountId())
                .personalPoints(newUser.getPersonalPoints())
                .build();
        
        return LoginVO.builder()
                .token(token)
                .userInfo(userInfo)
                .build();
    }
}
