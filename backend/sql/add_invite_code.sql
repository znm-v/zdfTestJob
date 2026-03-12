-- 添加邀请码

USE sweetbite;

-- 为情侣账户 1 生成邀请码
INSERT INTO `invite_code` (`couple_account_id`, `code`, `status`)
VALUES (1, 'INVITE123', 0);

-- 查看邀请码
SELECT * FROM `invite_code`;
