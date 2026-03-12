package com.sweetbite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sweetbite.common.Constants;
import com.sweetbite.common.ErrorCode;
import com.sweetbite.common.PageResult;
import com.sweetbite.entity.Gift;
import com.sweetbite.exception.BusinessException;
import com.sweetbite.mapper.GiftMapper;
import com.sweetbite.service.IGiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 礼品服务实现
 */
@Service
@RequiredArgsConstructor
public class GiftServiceImpl extends ServiceImpl<GiftMapper, Gift> implements IGiftService {
    
    private final GiftMapper giftMapper;
    
    @Override
    @Cacheable(value = "gift:list", key = "#current + ':' + #size")
    public PageResult<Gift> listGift(Long current, Long size) {
        Page<Gift> page = new Page<>(current, size);
        IPage<Gift> result = giftMapper.selectPage(page, 
                new LambdaQueryWrapper<Gift>()
                        .eq(Gift::getStatus, Constants.STATUS_ENABLED)
                        .orderByDesc(Gift::getSales));
        
        return new PageResult<>(result.getRecords(), result.getTotal(), 
                result.getCurrent(), result.getSize());
    }
    
    @Override
    public Gift getGiftDetail(Long id) {
        Gift gift = giftMapper.selectById(id);
        if (gift == null) {
            throw new BusinessException(ErrorCode.GIFT_NOT_FOUND);
        }
        return gift;
    }
    
    @Override
    public Gift createGift(Gift gift) {
        // 设置默认值
        if (gift.getStock() == null) {
            gift.setStock(999);
        }
        if (gift.getSales() == null) {
            gift.setSales(0);
        }
        if (gift.getStatus() == null) {
            gift.setStatus(Constants.STATUS_ENABLED);
        }
        
        giftMapper.insert(gift);
        return gift;
    }
    
    @Override
    public void deleteGift(Long id) {
        Gift gift = giftMapper.selectById(id);
        if (gift == null) {
            throw new BusinessException(ErrorCode.GIFT_NOT_FOUND);
        }
        giftMapper.deleteById(id);
    }
}
