package com.sweetbite.controller;

import com.sweetbite.annotation.RequireAdmin;
import com.sweetbite.common.Result;
import com.sweetbite.dto.GrantPointsDTO;
import com.sweetbite.entity.CoupleAccount;
import com.sweetbite.service.ICoupleAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理员控制器
 */
@Api(tags = "管理员接口")
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final ICoupleAccountService coupleAccountService;
    private final com.sweetbite.mapper.UserMapper userMapper;
    
    @ApiOperation("发放积分（管理员）")
    @PostMapping("/points/grant")
    @RequireAdmin
    public Result<Map<String, Object>> grantPoints(@Validated @RequestBody GrantPointsDTO dto) {
        // 获取目标用户
        com.sweetbite.entity.User user = userMapper.selectById(dto.getUserId());
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        Integer beforePoints = user.getPersonalPoints();
        
        // 更新用户积分
        user.setPersonalPoints(beforePoints + dto.getPoints());
        userMapper.updateById(user);
        
        // 构建响应
        Map<String, Object> result = new HashMap<>();
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("nickname", user.getNickname());
        result.put("beforePoints", beforePoints);
        result.put("afterPoints", user.getPersonalPoints());
        result.put("changePoints", dto.getPoints());
        
        return Result.success("积分发放成功", result);
    }
    
    @ApiOperation("获取统计数据（管理员）")
    @GetMapping("/statistics")
    @RequireAdmin
    public Result<Map<String, Object>> getStatistics() {
        // 这里可以实现统计逻辑
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalUsers", 100);
        statistics.put("totalCouples", 50);
        statistics.put("totalOrders", 1250);
        statistics.put("totalPoints", 125000);
        statistics.put("todayOrders", 45);
        statistics.put("todayRevenue", 12500);
        
        return Result.success(statistics);
    }
    
    @ApiOperation("获取所有用户（管理员）")
    @GetMapping("/users")
    @RequireAdmin
    public Result<java.util.List<Map<String, Object>>> getAllUsers() {
        // 获取所有用户
        java.util.List<com.sweetbite.entity.User> users = userMapper.selectList(null);
        
        java.util.List<Map<String, Object>> result = new java.util.ArrayList<>();
        for (com.sweetbite.entity.User user : users) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("username", user.getUsername());
            userMap.put("nickname", user.getNickname());
            userMap.put("role", user.getRole());
            userMap.put("personalPoints", user.getPersonalPoints() != null ? user.getPersonalPoints() : 0);
            result.add(userMap);
        }
        
        return Result.success(result);
    }
}
