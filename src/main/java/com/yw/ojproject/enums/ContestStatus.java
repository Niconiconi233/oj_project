package com.yw.ojproject.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ContestStatus {
    CONTEST_NOT_START(1,"Contest not start"),
    CONTEST_END(-1, "Contest ended"),
    CONTEST_UNDERWAY(0, "Starting");

    private Integer code;
    private String desc;
}
