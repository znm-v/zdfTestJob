package com.sweetbite.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 情侣账户实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("couple_account")
public class CoupleAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String coupleName;
    
    private String avatarUrl;
    
    private LocalDate togetherDate;
    
    private Integer totalPoints;
    
    private Integer usedPoints;
    
    private Integer availablePoints;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
