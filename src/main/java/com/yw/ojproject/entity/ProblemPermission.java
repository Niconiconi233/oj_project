package com.yw.ojproject.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum ProblemPermission {
    NONE(0, "none"),
    OWN(1, "own"),
    ALL(2, "all");

    private int value;
    private String desc;

    @JsonValue
    public int value(){
        return value;
    }
    public String desc() {
        return desc;
    }
}
