package com.yw.ojproject.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-04-14 17:05
**/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileBo {
    private String real_name;
    private String mood;
    private String major;
    private String blog;
    private String school;
    private String github;
    private String language;
}
