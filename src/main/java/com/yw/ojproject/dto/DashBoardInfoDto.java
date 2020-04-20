package com.yw.ojproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
* @program: ojproject
*
* @description: 信息表DTO
*
* @author: YW
*
* @create: 2020-03-24 21:11
**/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashBoardInfoDto {
    private Integer user_count = 10;
    private Integer recent_contest_count = 10;
    private Integer today_submission_count = 10;
    private Integer judge_server_count = 10;
    private Map<String, Object> env = new HashMap<>();
}
