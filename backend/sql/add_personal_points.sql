-- 添加个人积分字段

USE sweetbite;

-- 给 user 表添加个人积分字段
ALTER TABLE `user` ADD COLUMN `personal_points` INT DEFAULT 0 COMMENT '个人积分' AFTER `role`;

-- 给现有用户设置初始积分
UPDATE `user` SET `personal_points` = 500 WHERE `role` = 'ADMIN';
UPDATE `user` SET `personal_points` = 0 WHERE `role` = 'USER';

-- 查看结果
SELECT id, username, nickname, role, personal_points FROM user;
