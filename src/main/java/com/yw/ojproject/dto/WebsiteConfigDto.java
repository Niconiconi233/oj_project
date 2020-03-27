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
    private String website_base_url;
    private String website_name;
    private String website_name_shortcut;
    private String website_footer;
    private Boolean allow_register;
    private Boolean submission_list_show_all;
}
