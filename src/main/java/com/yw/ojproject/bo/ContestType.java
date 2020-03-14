package com.yw.ojproject.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ContestType {
    PUBLIC(0,"Public"),
    PASSWORD_PROTECTED_CONTEST(1,"Password Protected");

    private Integer code;
    private String desc;
}
