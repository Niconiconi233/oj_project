package com.yw.ojproject.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ProblemIOMode {
    standard(0,"Standard IO"),
    file(1,"File IO");

    private Integer code;
    private String desc;
}