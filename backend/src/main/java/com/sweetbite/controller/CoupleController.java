package com.sweetbite.controller;

import com.sweetbite.common.PageResult;
import com.sweetbite.common.Result;
import com.sweetbite.entity.PointsLog;
import com.sweetbite.entity.User;
import com.sweetbite.mapper.UserMapper;
import com.sweetbite.service.ICoupleAccountService;
import com.sweetbite.utils.JwtUtil;
import com.sweetbite.vo.CoupleAccountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 情侣账户控制器
 */
@Api(tags = "情侣账户接口")
@RestController
@RequestMapping("/api/v1/couple")
@RequiredArgsConstructor
public class CoupleController {
    
    private final ICoupleAccountService coupleAccountService;
    private final UserMapper userMapper;
    
    @ApiOperation("获取情侣账户信息")
    @GetMapping("/info")
    public Result<CoupleAccountVO> getCoupleAccountInfo(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        User user = userMapper.selectById(userId);
        CoupleAccountVO vo = coupleAccountService.getCoupleAccountInfo(user.getCoupleAccountId());
        return Result.success(vo);
    }
    
    @ApiOperation("获取积分流水")
    @GetMapping("/points/log")
    public Result<PageResult<PointsLog>> getPointsLog(
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long size) {
        Long userId = getUserIdFromRequest(request);
        User user = userMapper.selectById(userId);
        PageResult<PointsLog> result = coupleAccountService.getPointsLog(
                user.getCoupleAccountId(), page, size);
        return Result.success(result);
    }
    
    private Long getUserIdFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return JwtUtil.getUserIdFromToken(token);
        }
        return null;
    }
}
