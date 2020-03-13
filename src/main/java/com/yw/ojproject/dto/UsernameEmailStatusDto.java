package com.yw.ojproject.dto;

import com.sun.org.apache.xpath.internal.operations.Bool;
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
* @create: 2020-03-12 21:04
**/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsernameEmailStatusDto {
    Boolean username;
    Boolean email;
}
