-- 重置邀请码

USE sweetbite;

-- 删除旧的邀请码
DELETE FROM invite_code;

-- 插入新的邀请码
INSERT INTO `invite_code` (`couple_account_id`, `code`, `status`)
VALUES (1, 'SWEET2024', 0);

-- 查看邀请码
SELECT * FROM invite_code;
