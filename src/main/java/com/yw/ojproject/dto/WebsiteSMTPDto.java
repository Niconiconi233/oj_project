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
* @create: 2020-03-27 15:55
**/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebsiteSMTPDto {
    private String server = "";
    private Integer port = 0;
    private String password = "";
    private String email = "";
    private Boolean tls = false;
}
