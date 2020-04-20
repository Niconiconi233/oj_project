package com.yw.ojproject.bo;

import com.yw.ojproject.enums.ProblemIOMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
* @program: ojproject
*
* @description: 问题模式BO
*
* @author: YW
*
* @create: 2020-03-14 17:10
**/
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProblemIOModeBo {
    private String io_mode = ProblemIOMode.standard.getDesc();
    private String input = "input.txt";
    private String output = "output.txt";

    public static ProblemIOModeBo default_io_mode()
    {
        return new ProblemIOModeBo();
    }
}


