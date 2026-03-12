package com.sweetbite.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 邀请码实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("invite_code")
public class InviteCode implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long coupleAccountId;
    
    private String code;
    
    private Integer status;  // 0-未使用 1-已使用
    
    private Long usedByUserId;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
