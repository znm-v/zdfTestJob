package com.sweetbite.common;

/**
 * 常量类
 */
public class Constants {
    
    // JWT相关
    public static final String JWT_SECRET = "sweetbite_secret_key_2024_for_jwt_token_generation_with_long_enough_length";
    public static final long JWT_EXPIRATION = 7 * 24 * 60 * 60 * 1000L; // 7天
    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";
    
    // 用户角色
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";
    
    // 订单类型
    public static final int ORDER_TYPE_FOOD = 1;  // 食物订单
    public static final int ORDER_TYPE_GIFT = 2;  // 礼品订单
    
    // 订单状态
    public static final int ORDER_STATUS_PENDING = 1;   // 待支付
    public static final int ORDER_STATUS_PAID = 2;      // 已支付
    public static final int ORDER_STATUS_COMPLETED = 3; // 已完成
    public static final int ORDER_STATUS_CANCELLED = 4; // 已取消
    
    // 订单项类型
    public static final int ITEM_TYPE_FOOD = 1;  // 食物
    public static final int ITEM_TYPE_GIFT = 2;  // 礼品
    
    // 积分变动类型
    public static final int POINTS_CHANGE_GRANT = 1;    // 系统发放
    public static final int POINTS_CHANGE_CONSUME = 2;  // 消费扣除
    public static final int POINTS_CHANGE_REFUND = 3;   // 退款返还
    public static final int POINTS_CHANGE_ADJUST = 4;   // 管理员调整
    
    // 商品状态
    public static final int STATUS_ENABLED = 1;   // 上架
    public static final int STATUS_DISABLED = 0;  // 下架
    
    // 库存
    public static final int STOCK_UNLIMITED = -1; // 无限库存
    
    // Redis缓存key
    public static final String CACHE_FOOD_LIST = "food:list:";
    public static final String CACHE_GIFT_LIST = "gift:list";
    public static final String CACHE_USER_INFO = "user:info:";
}
