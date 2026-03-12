package com.sweetbite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sweetbite.common.Constants;
import com.sweetbite.common.ErrorCode;
import com.sweetbite.common.PageResult;
import com.sweetbite.dto.CreateFoodDTO;
import com.sweetbite.entity.Food;
import com.sweetbite.exception.BusinessException;
import com.sweetbite.mapper.FoodMapper;
import com.sweetbite.service.IFoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 食物服务实现
 */
@Service
@RequiredArgsConstructor
public class FoodServiceImpl extends ServiceImpl<FoodMapper, Food> implements IFoodService {
    
    private final FoodMapper foodMapper;
    
    @Override
    @Cacheable(value = "food:list", key = "#category + ':' + #current + ':' + #size")
    public PageResult<Food> listFood(String category, Long current, Long size) {
        Page<Food> page = new Page<>(current, size);
        LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<Food>()
                .eq(Food::getStatus, Constants.STATUS_ENABLED)
                .orderByDesc(Food::getSales);
        
        if (StringUtils.hasText(category)) {
            wrapper.eq(Food::getCategory, category);
        }
        
        IPage<Food> result = foodMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), 
                result.getCurrent(), result.getSize());
    }
    
    @Override
    public Food getFoodDetail(Long id) {
        Food food = foodMapper.selectById(id);
        if (food == null) {
            throw new BusinessException(ErrorCode.FOOD_NOT_FOUND);
        }
        return food;
    }
    
    @Override
    @CacheEvict(value = "food:list", allEntries = true)
    public void createFood(CreateFoodDTO dto) {
        Food food = new Food();
        BeanUtils.copyProperties(dto, food);
        food.setSales(0);
        food.setStatus(Constants.STATUS_ENABLED);
        foodMapper.insert(food);
    }
    
    @Override
    @CacheEvict(value = "food:list", allEntries = true)
    public void updateFood(Long id, CreateFoodDTO dto) {
        Food food = foodMapper.selectById(id);
        if (food == null) {
            throw new BusinessException(ErrorCode.FOOD_NOT_FOUND);
        }
        
        BeanUtils.copyProperties(dto, food);
        foodMapper.updateById(food);
    }
    
    @Override
    @CacheEvict(value = "food:list", allEntries = true)
    public void deleteFood(Long id) {
        Food food = foodMapper.selectById(id);
        if (food == null) {
            throw new BusinessException(ErrorCode.FOOD_NOT_FOUND);
        }
        
        food.setStatus(Constants.STATUS_DISABLED);
        foodMapper.updateById(food);
    }
}
