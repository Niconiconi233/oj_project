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
* @create: 2020-03-14 14:49
**/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebsiteConfigDto {
    private String website_base_url = "http://localhost:8080";
    private String website_name = "OJ-Project";
    private String website_name_shortcut = "OJ";
    private String website_footer = "<p>Online Judge Project</p>";
    private Boolean allow_register = true;
    private Boolean submission_list_show_all = true;
}
