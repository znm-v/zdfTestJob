-- 测试数据插入脚本
USE sweetbite;

-- 1. 插入情侣账户
INSERT INTO `couple_account` (`couple_name`, `together_date`, `total_points`, `used_points`, `available_points`, `status`)
VALUES ('Felix & Marry', '2024-01-01', 1000, 0, 1000, 1);

-- 2. 插入测试用户
-- 用户名: felix, 密码: 123456 (BCrypt加密后)
-- BCrypt加密: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/TVm
INSERT INTO `user` (`couple_account_id`, `username`, `password`, `nickname`, `phone`, `role`, `status`)
VALUES (1, 'felix', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/TVm', 'Felix', '13800138000', 'ADMIN', 1);

-- 3. 插入第二个测试用户
-- 用户名: marry, 密码: 123456
INSERT INTO `user` (`couple_account_id`, `username`, `password`, `nickname`, `phone`, `role`, `status`)
VALUES (1, 'marry', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/TVm', 'Marry', '13800138001', 'USER', 1);

-- 4. 插入测试食物
INSERT INTO `food` (`name`, `description`, `image_url`, `category`, `points`, `stock`, `status`, `sort_order`)
VALUES 
('红烧肉', '美味的红烧肉', '', '热门', 50, -1, 1, 1),
('宫保鸡丁', '经典川菜', '', '热门', 40, -1, 1, 2),
('番茄鸡蛋面', '清汤面条', '', '主食', 30, -1, 1, 3),
('奶茶', '香浓奶茶', '', '饮品', 20, -1, 1, 4),
('提拉米苏', '意大利甜品', '', '甜品', 35, -1, 1, 5);

-- 5. 插入测试礼品
INSERT INTO `gift` (`name`, `description`, `image_url`, `points`, `stock`, `status`, `sort_order`)
VALUES 
('玫瑰花束', '99朵红玫瑰', '', 500, -1, 1, 1),
('情侣手链', '成对的手链', '', 300, -1, 1, 2),
('电影票', '两张电影票', '', 200, -1, 1, 3),
('香水', '高级香水', '', 400, -1, 1, 4);
