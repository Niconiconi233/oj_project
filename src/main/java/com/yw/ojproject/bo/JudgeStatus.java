package com.yw.ojproject.bo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum JudgeStatus {
    COMPILE_ERROR(-2),
    WRONG_ANSWER(-1),
    ACCEPTED(0),
    CPU_TIME_LIMIT_EXCEEDED(1),
    REAL_TIME_LIMIT_EXCEEDED(2),
    MEMORY_LIMIT_EXCEEDED(3),
    RUNTIME_ERROR(4),
    SYSTEM_ERROR(5),
    PENDING(6),
    JUDGING(7),
    PARTIALLY_ACCEPTED(8);
    private int value;
}
