package com.yw.ojproject.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @program: ojproject
*
* @description: 问题SCOREBO
*
* @author: YW
*
* @create: 2020-03-18 19:20
**/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemCaseScoreBo {
    private String input_name;
    private String output_name;
    private Integer score;
}
