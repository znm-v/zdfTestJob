package com.sweetbite.controller;

import com.sweetbite.annotation.RequireAdmin;
import com.sweetbite.common.PageResult;
import com.sweetbite.common.Result;
import com.sweetbite.dto.CreateFoodDTO;
import com.sweetbite.entity.Food;
import com.sweetbite.service.IFoodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 食物控制器
 */
@Api(tags = "食物接口")
@RestController
@RequestMapping("/api/v1/food")
@RequiredArgsConstructor
public class FoodController {
    
    private final IFoodService foodService;
    
    @ApiOperation("获取食物列表")
    @GetMapping("/list")
    public Result<PageResult<Food>> listFood(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long size) {
        PageResult<Food> result = foodService.listFood(category, page, size);
        return Result.success(result);
    }
    
    @ApiOperation("获取食物详情")
    @GetMapping("/{id}")
    public Result<Food> getFoodDetail(@PathVariable Long id) {
        Food food = foodService.getFoodDetail(id);
        return Result.success(food);
    }
    
    @ApiOperation("创建食物（管理员）")
    @PostMapping("/create")
    @RequireAdmin
    public Result<String> createFood(@Validated @RequestBody CreateFoodDTO dto) {
        foodService.createFood(dto);
        return Result.success("创建成功");
    }
    
    @ApiOperation("更新食物（管理员）")
    @PutMapping("/update/{id}")
    @RequireAdmin
    public Result<String> updateFood(@PathVariable Long id, 
                                    @Validated @RequestBody CreateFoodDTO dto) {
        foodService.updateFood(id, dto);
        return Result.success("更新成功");
    }
    
    @ApiOperation("删除食物（管理员）")
    @DeleteMapping("/{id}")
    @RequireAdmin
    public Result<String> deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
        return Result.success("删除成功");
    }
}
