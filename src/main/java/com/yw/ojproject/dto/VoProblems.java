package com.yw.ojproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @program: ojproject
*
* @description: problems 情况视图
*
* @author: YW
*
* @create: 2020-03-11 18:47
**/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoProblems {
    private Integer id;
    private Integer score;
    private String status;

}

