package com.yw.ojproject.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ContestRuleType {
    ACM(0, "ACM"),
    IO(1,"IO");

    private Integer code;
    private String desc;
}
