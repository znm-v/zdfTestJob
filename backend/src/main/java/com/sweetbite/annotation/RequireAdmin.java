package com.sweetbite.annotation;

import java.lang.annotation.*;

/**
 * 需要管理员权限的注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireAdmin {
    String value() default "需要管理员权限";
}
