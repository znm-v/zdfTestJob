package com.sweetbite.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sweetbite.common.PageResult;
import com.sweetbite.entity.Gift;

/**
 * 礼品服务接口
 */
public interface IGiftService extends IService<Gift> {
    
    /**
     * 获取礼品列表
     */
    PageResult<Gift> listGift(Long current, Long size);
    
    /**
     * 获取礼品详情
     */
    Gift getGiftDetail(Long id);
    
    /**
     * 创建礼品
     */
    Gift createGift(Gift gift);
    
    /**
     * 删除礼品
     */
    void deleteGift(Long id);
}
