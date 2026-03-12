package com.sweetbite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sweetbite.common.Constants;
import com.sweetbite.common.ErrorCode;
import com.sweetbite.common.PageResult;
import com.sweetbite.entity.CoupleAccount;
import com.sweetbite.entity.PointsLog;
import com.sweetbite.exception.BusinessException;
import com.sweetbite.mapper.CoupleAccountMapper;
import com.sweetbite.mapper.PointsLogMapper;
import com.sweetbite.service.ICoupleAccountService;
import com.sweetbite.vo.CoupleAccountVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 情侣账户服务实现
 */
@Service
@RequiredArgsConstructor
public class CoupleAccountServiceImpl extends ServiceImpl<CoupleAccountMapper, CoupleAccount> 
        implements ICoupleAccountService {
    
    private final CoupleAccountMapper coupleAccountMapper;
    private final PointsLogMapper pointsLogMapper;
    
    @Override
    public CoupleAccountVO getCoupleAccountInfo(Long coupleAccountId) {
        CoupleAccount coupleAccount = coupleAccountMapper.selectById(coupleAccountId);
        if (coupleAccount == null) {
            throw new BusinessException(ErrorCode.COUPLE_ACCOUNT_NOT_FOUND);
        }
        
        CoupleAccountVO vo = new CoupleAccountVO();
        BeanUtils.copyProperties(coupleAccount, vo);
        return vo;
    }
    
    @Override
    public PageResult<PointsLog> getPointsLog(Long coupleAccountId, Long current, Long size) {
        Page<PointsLog> page = new Page<>(current, size);
        IPage<PointsLog> result = pointsLogMapper.selectPage(page, 
                new LambdaQueryWrapper<PointsLog>()
                        .eq(PointsLog::getCoupleAccountId, coupleAccountId)
                        .orderByDesc(PointsLog::getCreateTime));
        
        return new PageResult<>(result.getRecords(), result.getTotal(), 
                result.getCurrent(), result.getSize());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deductPoints(Long coupleAccountId, Integer points, String remark) {
        CoupleAccount account = coupleAccountMapper.selectById(coupleAccountId);
        if (account == null) {
            throw new BusinessException(ErrorCode.COUPLE_ACCOUNT_NOT_FOUND);
        }
        
        if (account.getAvailablePoints() < points) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_POINTS);
        }
        
        Integer beforePoints = account.getAvailablePoints();
        Integer afterPoints = beforePoints - points;
        
        // 更新账户积分
        account.setUsedPoints(account.getUsedPoints() + points);
        account.setAvailablePoints(afterPoints);
        coupleAccountMapper.updateById(account);
        
        // 记录流水
        PointsLog log = PointsLog.builder()
                .coupleAccountId(coupleAccountId)
                .changeType(Constants.POINTS_CHANGE_CONSUME)
                .changePoints(-points)
                .beforePoints(beforePoints)
                .afterPoints(afterPoints)
                .remark(remark)
                .build();
        pointsLogMapper.insert(log);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPoints(Long coupleAccountId, Integer points, String remark) {
        CoupleAccount account = coupleAccountMapper.selectById(coupleAccountId);
        if (account == null) {
            throw new BusinessException(ErrorCode.COUPLE_ACCOUNT_NOT_FOUND);
        }
        
        Integer beforePoints = account.getAvailablePoints();
        Integer afterPoints = beforePoints + points;
        
        // 更新账户积分
        account.setTotalPoints(account.getTotalPoints() + points);
        account.setAvailablePoints(afterPoints);
        coupleAccountMapper.updateById(account);
        
        // 记录流水
        PointsLog log = PointsLog.builder()
                .coupleAccountId(coupleAccountId)
                .changeType(Constants.POINTS_CHANGE_GRANT)
                .changePoints(points)
                .beforePoints(beforePoints)
                .afterPoints(afterPoints)
                .remark(remark)
                .build();
        pointsLogMapper.insert(log);
    }
}
