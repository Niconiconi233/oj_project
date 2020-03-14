package com.yw.ojproject.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-14 17:10
**/
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProblemIOModeBo {
    private ProblemIOMode io_mode = ProblemIOMode.standard;
    private String input = "input.txt";
    private String output = "output.txt";

    public static ProblemIOModeBo default_io_mode()
    {
        return new ProblemIOModeBo();
    }
}


