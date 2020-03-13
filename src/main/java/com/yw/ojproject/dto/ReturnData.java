package com.yw.ojproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-11 22:14
**/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnData {
    private String error;
    private Object data;
}
