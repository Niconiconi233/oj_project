package com.yw.ojproject.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum JudgeStatus {
    COMPILE_ERROR(-2, "COMPILE_ERROR"),
    WRONG_ANSWER(-1, "WRONG_ANSWER"),
    ACCEPTED(0, "ACCEPTED"),
    CPU_TIME_LIMIT_EXCEEDED(1, "CPU_TIME_LIMIT_EXCEEDED"),
    REAL_TIME_LIMIT_EXCEEDED(2,"REAL_TIME_LIMIT_EXCEEDED"),
    MEMORY_LIMIT_EXCEEDED(3, "MEMORY_LIMIT_EXCEEDED"),
    RUNTIME_ERROR(4, "RUNTIME_ERROR"),
    SYSTEM_ERROR(5, "SYSTEM_ERROR"),
    PENDING(6, "PENDING"),
    JUDGING(7, "JUDGING"),
    PARTIALLY_ACCEPTED(8, "PARTIALLY_ACCEPTED");
    private Integer code;
    private String desc;

    public static JudgeStatus getJudgeStatusEnumByCode(Integer code){
        for(JudgeStatus diff : JudgeStatus.values()){
            if(code.equals(diff.getCode())){
                return diff;
            }
        }
        return null;
    }
}
