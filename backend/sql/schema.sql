-- SweetBite 数据库设计
-- MySQL 8.0+

-- 创建数据库
CREATE DATABASE IF NOT EXISTS sweetbite DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sweetbite;

-- 1. 情侣账户表
CREATE TABLE `couple_account` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `couple_name` VARCHAR(100) NOT NULL COMMENT '情侣昵称（如：Felix & Marry）',
  `avatar_url` VARCHAR(500) COMMENT '头像URL',
  `together_days` INT DEFAULT 0 COMMENT '在一起天数',
  `together_date` DATE COMMENT '在一起的日期',
  `total_points` INT DEFAULT 0 COMMENT '总积分',
  `used_points` INT DEFAULT 0 COMMENT '已使用积分',
  `available_points` INT DEFAULT 0 COMMENT '可用积分',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='情侣账户表';

-- 2. 用户表
CREATE TABLE `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `couple_account_id` BIGINT NOT NULL COMMENT '情侣账户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码（加密）',
  `nickname` VARCHAR(50) COMMENT '昵称',
  `phone` VARCHAR(20) COMMENT '手机号',
  `email` VARCHAR(100) COMMENT '邮箱',
  `avatar` VARCHAR(500) COMMENT '头像',
  `gender` TINYINT COMMENT '性别：0-女，1-男',
  `role` VARCHAR(20) DEFAULT 'USER' COMMENT '角色：USER-用户，ADMIN-管理员',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
  `last_login_time` DATETIME COMMENT '最后登录时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_couple_account_id` (`couple_account_id`),
  KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 3. 食物表
CREATE TABLE `food` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` VARCHAR(100) NOT NULL COMMENT '食物名称',
  `description` VARCHAR(500) COMMENT '描述',
  `image_url` VARCHAR(500) COMMENT '图片URL',
  `category` VARCHAR(50) NOT NULL COMMENT '分类：热门、主食、饮品、甜品',
  `points` INT NOT NULL COMMENT '所需积分',
  `stock` INT DEFAULT -1 COMMENT '库存：-1表示无限',
  `sales` INT DEFAULT 0 COMMENT '销量',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-下架，1-上架',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_category` (`category`),
  KEY `idx_status` (`status`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='食物表';

-- 4. 礼品表
CREATE TABLE `gift` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` VARCHAR(100) NOT NULL COMMENT '礼品名称',
  `description` VARCHAR(500) COMMENT '描述',
  `image_url` VARCHAR(500) COMMENT '图片URL',
  `points` INT NOT NULL COMMENT '所需积分',
  `stock` INT DEFAULT -1 COMMENT '库存：-1表示无限',
  `sales` INT DEFAULT 0 COMMENT '兑换次数',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-下架，1-上架',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='礼品表';

-- 5. 订单表
CREATE TABLE `order` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_no` VARCHAR(50) NOT NULL COMMENT '订单号',
  `couple_account_id` BIGINT NOT NULL COMMENT '情侣账户ID',
  `user_id` BIGINT NOT NULL COMMENT '下单用户ID',
  `order_type` TINYINT NOT NULL COMMENT '订单类型：1-点餐，2-兑换',
  `total_points` INT NOT NULL COMMENT '总积分',
  `item_count` INT NOT NULL COMMENT '商品数量',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-已取消，1-待支付，2-已支付，3-已完成',
  `pay_time` DATETIME COMMENT '支付时间',
  `complete_time` DATETIME COMMENT '完成时间',
  `cancel_time` DATETIME COMMENT '取消时间',
  `remark` VARCHAR(500) COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_couple_account_id` (`couple_account_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- 6. 订单明细表
CREATE TABLE `order_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` BIGINT NOT NULL COMMENT '订单ID',
  `item_type` TINYINT NOT NULL COMMENT '商品类型：1-食物，2-礼品',
  `item_id` BIGINT NOT NULL COMMENT '商品ID',
  `item_name` VARCHAR(100) NOT NULL COMMENT '商品名称',
  `item_image` VARCHAR(500) COMMENT '商品图片',
  `points` INT NOT NULL COMMENT '单价积分',
  `quantity` INT DEFAULT 1 COMMENT '数量',
  `total_points` INT NOT NULL COMMENT '小计积分',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_item_type_id` (`item_type`, `item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单明细表';

-- 7. 积分流水表
CREATE TABLE `points_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `couple_account_id` BIGINT NOT NULL COMMENT '情侣账户ID',
  `user_id` BIGINT COMMENT '操作用户ID',
  `change_type` TINYINT NOT NULL COMMENT '变动类型：1-系统发放，2-消费扣除，3-退款返还，4-管理员调整',
  `change_points` INT NOT NULL COMMENT '变动积分（正数为增加，负数为减少）',
  `before_points` INT NOT NULL COMMENT '变动前积分',
  `after_points` INT NOT NULL COMMENT '变动后积分',
  `related_order_id` BIGINT COMMENT '关联订单ID',
  `remark` VARCHAR(500) COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_couple_account_id` (`couple_account_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='积分流水表';

-- 8. 系统配置表
CREATE TABLE `system_config` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_key` VARCHAR(100) NOT NULL COMMENT '配置键',
  `config_value` TEXT COMMENT '配置值',
  `config_desc` VARCHAR(200) COMMENT '配置描述',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- 8. 邀请码表
CREATE TABLE `invite_code` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `couple_account_id` BIGINT NOT NULL COMMENT '情侣账户ID',
  `code` VARCHAR(50) NOT NULL COMMENT '邀请码',
  `status` TINYINT DEFAULT 0 COMMENT '状态：0-未使用，1-已使用',
  `used_by_user_id` BIGINT COMMENT '使用者用户ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`),
  KEY `idx_couple_account_id` (`couple_account_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='邀请码表';

-- 初始化数据

-- 插入测试情侣账户
INSERT INTO `couple_account` (`couple_name`, `together_days`, `together_date`, `total_points`, `available_points`) 
VALUES ('Felix & Marry', 520, '2023-01-01', 2480, 2480);

-- 插入测试用户
INSERT INTO `user` (`couple_account_id`, `username`, `password`, `nickname`, `role`) 
VALUES 
(1, 'felix', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Felix', 'ADMIN'),
(1, 'marry', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Marry', 'USER');
-- 密码都是: 123456

-- 插入测试食物
INSERT INTO `food` (`name`, `description`, `image_url`, `category`, `points`, `sales`, `sort_order`) VALUES
('蜜汁烤鸡翅', '外酥里嫩，甜而不腻，情侣必点', 'https://images.unsplash.com/photo-1527477396000-e27163b481c2?w=400', '热门', 280, 156, 1),
('草莓奶昔', '新鲜草莓+香浓牛奶，甜蜜满分', 'https://images.unsplash.com/photo-1572490122747-3968b75cc699?w=400', '饮品', 180, 203, 2),
('意式千层面', '层层叠叠的爱意，浓郁芝士香', 'https://images.unsplash.com/photo-1621996346565-e3dbc646d9a9?w=400', '主食', 320, 89, 3),
('提拉米苏', '经典意式甜品，带你去意大利', 'https://images.unsplash.com/photo-1571877227200-a0d98ea607e9?w=400', '甜品', 240, 134, 4);

-- 插入测试礼品
INSERT INTO `gift` (`name`, `description`, `image_url`, `points`, `sales`, `sort_order`) VALUES
('情侣马克杯套装', '精选心意好礼，记录美好瞬间', 'https://images.unsplash.com/photo-1514228742587-6b1558fcca3d?w=400', 1200, 23, 1),
('定制相册', '精选心意好礼，记录美好瞬间', 'https://images.unsplash.com/photo-1512820790803-83ca734da794?w=400', 1800, 15, 2),
('香薰蜡烛礼盒', '精选心意好礼，记录美好瞬间', 'https://images.unsplash.com/photo-1602874801006-e04b6d0c5d8f?w=400', 980, 31, 3);
