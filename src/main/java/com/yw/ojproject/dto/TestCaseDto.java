package com.yw.ojproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-27 16:26
**/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseDto {
    private String id;
    private Date create_time;
}
