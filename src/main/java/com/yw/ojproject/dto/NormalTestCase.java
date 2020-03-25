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
* @create: 2020-03-21 16:12
**/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NormalTestCase {
    private String stripped_output_md5;
    private Integer output_size;
    private String input_name;
    private Integer input_size;
    private String output_name;
}
