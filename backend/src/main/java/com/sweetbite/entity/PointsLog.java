package com.sweetbite.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 积分流水实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("points_log")
public class PointsLog implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long coupleAccountId;
    
    private Integer changeType;
    
    private Integer changePoints;
    
    private Integer beforePoints;
    
    private Integer afterPoints;
    
    private String remark;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
