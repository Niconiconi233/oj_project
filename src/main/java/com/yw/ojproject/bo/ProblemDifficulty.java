package com.yw.ojproject.bo;

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
}
