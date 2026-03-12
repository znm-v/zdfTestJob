package com.sweetbite.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sweetbite.common.PageResult;
import com.sweetbite.dto.CreateFoodDTO;
import com.sweetbite.entity.Food;

/**
 * 食物服务接口
 */
public interface IFoodService extends IService<Food> {
    
    /**
     * 获取食物列表
     */
    PageResult<Food> listFood(String category, Long current, Long size);
    
    /**
     * 获取食物详情
     */
    Food getFoodDetail(Long id);
    
    /**
     * 创建食物
     */
    void createFood(CreateFoodDTO dto);
    
    /**
     * 更新食物
     */
    void updateFood(Long id, CreateFoodDTO dto);
    
    /**
     * 删除食物
     */
    void deleteFood(Long id);
}
