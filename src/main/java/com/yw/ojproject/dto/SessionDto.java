package com.yw.ojproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
* @program: ojproject
*
* @description: sessionDTO
*
* @author: YW
*
* @create: 2020-03-13 16:17
**/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionDto {
    private String ip;
    private String user_agent;
    private Date last_activity;
    private String session_key;
}
