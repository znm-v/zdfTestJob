package com.sweetbite.aspect;

import com.sweetbite.annotation.RequireAdmin;
import com.sweetbite.common.ErrorCode;
import com.sweetbite.exception.BusinessException;
import com.sweetbite.mapper.UserMapper;
import com.sweetbite.entity.User;
import com.sweetbite.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 管理员权限检查切面
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AdminCheckAspect {
    
    private final UserMapper userMapper;
    
    @Before("@annotation(requireAdmin)")
    public void checkAdmin(JoinPoint joinPoint, RequireAdmin requireAdmin) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("Authorization");
        
        if (token == null || !token.startsWith("Bearer ")) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        
        token = token.substring(7);
        Long userId = JwtUtil.getUserIdFromToken(token);
        
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        
        User user = userMapper.selectById(userId);
        if (user == null || !"ADMIN".equals(user.getRole())) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        
        log.info("管理员权限检查通过: userId={}", userId);
    }
}
