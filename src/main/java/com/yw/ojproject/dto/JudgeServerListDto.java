package com.yw.ojproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
* @program: ojproject
*
* @description: 评测服务器列表DTO
*
* @author: YW
*
* @create: 2020-03-31 15:40
**/
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JudgeServerListDto {
    private String token;
    private List<JudgeServerDto> servers;
}
