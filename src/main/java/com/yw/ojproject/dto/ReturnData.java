package com.yw.ojproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @program: ojproject
*
* @description: 返回类型DTO
*
* @author: YW
*
* @create: 2020-03-11 22:14
**/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnData {
    private Object error;
    private Object data;
}
