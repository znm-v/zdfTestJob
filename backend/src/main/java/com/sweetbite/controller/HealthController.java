package com.sweetbite.controller;

import com.sweetbite.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查控制器
 */
@Api(tags = "健康检查")
@RestController
@RequestMapping("/api/v1/health")
public class HealthController {
    
    @ApiOperation("健康检查")
    @GetMapping("/check")
    public Result<String> healthCheck() {
        return Result.success("系统正常运行");
    }
}
