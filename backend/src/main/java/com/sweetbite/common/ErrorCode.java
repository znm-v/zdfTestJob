package com.sweetbite.common;

/**
 * 错误码枚举
 */
public enum ErrorCode {
    
    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "无权限"),
    NOT_FOUND(404, "资源不存在"),
    INTERNAL_ERROR(500, "服务器内部错误"),
    
    // 业务错误码 1000+
    INSUFFICIENT_POINTS(1001, "积分不足"),
    INSUFFICIENT_STOCK(1002, "库存不足"),
    ORDER_STATUS_ERROR(1003, "订单状态错误"),
    USERNAME_EXISTS(1004, "用户名已存在"),
    USER_NOT_FOUND(1005, "用户不存在"),
    PASSWORD_ERROR(1006, "密码错误"),
    FOOD_NOT_FOUND(1007, "食物不存在"),
    GIFT_NOT_FOUND(1008, "礼品不存在"),
    ORDER_NOT_FOUND(1009, "订单不存在"),
    COUPLE_ACCOUNT_NOT_FOUND(1010, "情侣账户不存在"),
    CART_ITEM_NOT_FOUND(1011, "购物车项不存在");
    
    private final Integer code;
    private final String message;
    
    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
}
