package com.yw.ojproject.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @program: ojproject
*
* @description: spjBO
*
* @author: YW
*
* @create: 2020-03-31 21:24
**/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompileSPJBo {
    private String spj_code;
    private String spj_language;
}
