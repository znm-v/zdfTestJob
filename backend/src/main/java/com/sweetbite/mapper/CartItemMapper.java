package com.sweetbite.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sweetbite.entity.CartItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 购物车Mapper
 */
@Mapper
public interface CartItemMapper extends BaseMapper<CartItem> {
}
