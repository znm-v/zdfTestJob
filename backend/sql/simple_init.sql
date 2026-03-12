-- 简单初始化脚本 - 使用简单的 BCrypt 密码

USE sweetbite;

-- 清空用户表
DELETE FROM `user`;
DELETE FROM `couple_account`;

-- 重置自增ID
ALTER TABLE `couple_account` AUTO_INCREMENT = 1;
ALTER TABLE `user` AUTO_INCREMENT = 1;

-- 插入情侣账户
INSERT INTO `couple_account` (`couple_name`, `together_date`, `total_points`, `used_points`, `available_points`, `status`)
VALUES ('Felix & Marry', '2024-01-01', 1000, 0, 1000, 1);

-- 插入测试用户
-- 密码: 123456
-- BCrypt: $2a$10$dXJ3SUoxRHBWWGg0VHBGLuIHVrD5KwDHF8z/LewKSUU9PxXo6KJIK
INSERT INTO `user` (`couple_account_id`, `username`, `password`, `nickname`, `phone`, `role`, `status`)
VALUES (1, 'felix', '$2a$10$dXJ3SUoxRHBWWGg0VHBGLuIHVrD5KwDHF8z/LewKSUU9PxXo6KJIK', 'Felix', '13800138000', 'ADMIN', 1);

-- 插入第二个测试用户
INSERT INTO `user` (`couple_account_id`, `username`, `password`, `nickname`, `phone`, `role`, `status`)
VALUES (1, 'marry', '$2a$10$dXJ3SUoxRHBWWGg0VHBGLuIHVrD5KwDHF8z/LewKSUU9PxXo6KJIK', 'Marry', '13800138001', 'USER', 1);
