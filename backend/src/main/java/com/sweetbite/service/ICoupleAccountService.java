package com.sweetbite.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sweetbite.common.PageResult;
import com.sweetbite.entity.CoupleAccount;
import com.sweetbite.entity.PointsLog;
import com.sweetbite.vo.CoupleAccountVO;

/**
 * 情侣账户服务接口
 */
public interface ICoupleAccountService extends IService<CoupleAccount> {
    
    /**
     * 获取情侣账户信息
     */
    CoupleAccountVO getCoupleAccountInfo(Long coupleAccountId);
    
    /**
     * 获取积分流水
     */
    PageResult<PointsLog> getPointsLog(Long coupleAccountId, Long current, Long size);
    
    /**
     * 扣除积分
     */
    void deductPoints(Long coupleAccountId, Integer points, String remark);
    
    /**
     * 增加积分
     */
    void addPoints(Long coupleAccountId, Integer points, String remark);
}
