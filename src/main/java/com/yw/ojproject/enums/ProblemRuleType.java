package com.yw.ojproject.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ProblemRuleType {
    ACM(0,"ACM"),
    IO(1,"IO");

    private Integer code;
    private String desc;
}
