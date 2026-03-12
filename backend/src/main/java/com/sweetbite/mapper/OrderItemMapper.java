package com.sweetbite.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sweetbite.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单明细Mapper
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
}
