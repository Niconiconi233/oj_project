package com.yw.ojproject.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ProblemIOMode {
    standard(0,"Standard IO"),
    file(1,"File IO");

    private Integer code;
    private String desc;
}