package com.sweetbite.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * 情侣账户响应VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoupleAccountVO {
    private Long id;
    private String coupleName;
    private String avatarUrl;
    private LocalDate togetherDate;
    private Long togetherDays;
    private Integer totalPoints;
    private Integer usedPoints;
    private Integer availablePoints;
    
    /**
     * 计算在一起天数
     */
    public Long getTogetherDays() {
        if (togetherDate != null) {
            return ChronoUnit.DAYS.between(togetherDate, LocalDate.now());
        }
        return 0L;
    }
}
