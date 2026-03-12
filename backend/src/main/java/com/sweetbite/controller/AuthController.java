package com.sweetbite.controller;

import com.sweetbite.annotation.RequireAdmin;
import com.sweetbite.common.Result;
import com.sweetbite.dto.LoginDTO;
import com.sweetbite.dto.RegisterDTO;
import com.sweetbite.dto.WechatLoginDTO;
import com.sweetbite.dto.InviteUserDTO;
import com.sweetbite.service.IAuthService;
import com.sweetbite.utils.JwtUtil;
import com.sweetbite.vo.LoginVO;
import com.sweetbite.vo.UserInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 认证控制器
 */
@Api(tags = "认证接口")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final IAuthService authService;
    
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Validated @RequestBody LoginDTO dto) {
        LoginVO loginVO = authService.login(dto);
        return Result.success("登录成功", loginVO);
    }
    
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result<String> register(@Validated @RequestBody RegisterDTO dto) {
        authService.register(dto);
        return Result.success("注册成功");
    }
    
    @ApiOperation("微信登录")
    @PostMapping("/wechat/login")
    public Result<LoginVO> wechatLogin(@Validated @RequestBody WechatLoginDTO dto) {
        LoginVO loginVO = authService.wechatLogin(dto);
        return Result.success("登录成功", loginVO);
    }
    
    @ApiOperation("邀请用户加入")
    @PostMapping("/invite")
    public Result<LoginVO> inviteUser(@Validated @RequestBody InviteUserDTO dto) {
        LoginVO loginVO = authService.inviteUser(dto);
        return Result.success("邀请成功", loginVO);
    }
    
    @ApiOperation("生成邀请码（管理员）")
    @PostMapping("/invite/generate")
    @RequireAdmin
    public Result<String> generateInviteCode(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        // 这里需要从用户获取coupleAccountId
        // 为了简化，这里假设已经获取
        String code = authService.generateInviteCode(1L);
        return Result.success("邀请码生成成功", code);
    }
    
    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public Result<UserInfoVO> getUserInfo(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        UserInfoVO userInfo = authService.getUserInfo(userId);
        return Result.success(userInfo);
    }
    
    /**
     * 从请求中获取用户ID
     */
    private Long getUserIdFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return JwtUtil.getUserIdFromToken(token);
        }
        return null;
    }
}
