package com.yw.ojproject.bo;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum AdminType {
    REGULAR_USER(0, "regular user"),
    ADMIN(1, "admin"),
    SUPER_ADMIN(2, "super admin");
    private int value;
    private String desc;

    @JsonValue
    public int value() {
        return value;
    }
    public String desc() {
        return desc;
    }
}
