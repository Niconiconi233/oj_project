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
* @create: 2020-03-14 14:49
**/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebsiteConfigDto {
    private String website_base_url = "http://localhost:8080";
    private String website_name = "test ojproject";
    private String website_name_shortcut = "ojproject";
    private String website_footer = "<p> test project </p>";
    private Boolean allow_register = true;
    private Boolean submission_list_show_all = true;
}
