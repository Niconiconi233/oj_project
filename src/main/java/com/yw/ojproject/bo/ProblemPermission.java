package com.yw.ojproject.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ProblemPermission {
    NONE(0, "None"),
    OWN(1, "Own"),
    ALL(2, "All");

    private int value;
    private String desc;
}
