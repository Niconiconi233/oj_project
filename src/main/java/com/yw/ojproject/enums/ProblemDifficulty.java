package com.yw.ojproject.enums;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ProblemDifficulty {
    HIGH(0, "High"),
    MID(1, "Mid"),
    LOW(2,"Low");

    private Integer code;
    private String desc;

    public static ProblemDifficulty getDifficultyEnumByCode(Integer code){
        for(ProblemDifficulty diff : ProblemDifficulty.values()){
            if(code.equals(diff.getCode())){
                return diff;
            }
        }
        return null;
    }
}
