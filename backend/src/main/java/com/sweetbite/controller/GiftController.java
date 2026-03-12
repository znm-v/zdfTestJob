package com.sweetbite.controller;

import com.sweetbite.common.PageResult;
import com.sweetbite.common.Result;
import com.sweetbite.entity.Gift;
import com.sweetbite.service.IGiftService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 礼品控制器
 */
@Api(tags = "礼品接口")
@RestController
@RequestMapping("/api/v1/gift")
@RequiredArgsConstructor
public class GiftController {
    
    private final IGiftService giftService;
    
    @ApiOperation("获取礼品列表")
    @GetMapping("/list")
    public Result<PageResult<Gift>> listGift(
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long size) {
        PageResult<Gift> result = giftService.listGift(page, size);
        return Result.success(result);
    }
    
    @ApiOperation("获取礼品详情")
    @GetMapping("/{id}")
    public Result<Gift> getGiftDetail(@PathVariable Long id) {
        Gift gift = giftService.getGiftDetail(id);
        return Result.success(gift);
    }
    
    @ApiOperation("创建礼品（管理员）")
    @PostMapping("/create")
    public Result<Gift> createGift(@RequestBody Gift gift) {
        Gift created = giftService.createGift(gift);
        return Result.success("礼品创建成功", created);
    }
    
    @ApiOperation("删除礼品（管理员）")
    @DeleteMapping("/{id}")
    public Result<String> deleteGift(@PathVariable Long id) {
        giftService.deleteGift(id);
        return Result.success("礼品删除成功", "success");
    }
}
