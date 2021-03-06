package com.yw.ojproject.dto;

import com.yw.ojproject.entity.UserProfile;
import com.yw.ojproject.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-19 15:20
**/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRankDto {
    public UserRankDto(UserProfile userProfile)
    {
        this.id = userProfile.getId();
        this.user = new UserSimpleDto(userProfile.getUser());
        this.real_name = userProfile.getReal_name();
        this.acm_problems_status = JsonUtils.jsonStringToObject(userProfile.getAcm_problems_status(), Map.class);
        this.oi_problems_status = JsonUtils.jsonStringToObject(userProfile.getIo_problems_status(), Map.class);
        this.avatar = userProfile.getAvator();
        this.blog = userProfile.getBlog();
        this.mood = userProfile.getMood();
        this.github = userProfile.getGithub();
        this.school = userProfile.getSchool();
        this.major = userProfile.getMajor();
        this.language = userProfile.getLanguage();
        this.accepted_number = userProfile.getAcceptnumber();
        this.total_score = userProfile.getTotalscore();
        this.submission_number = userProfile.getSubmissionnumber();
    }
    private Integer id;
    private UserSimpleDto user;
    private String real_name;
    private Map<Integer, VoProblems> acm_problems_status;
    private Map<Integer, VoProblems> oi_problems_status;
    private String avatar;
    private String blog;
    private String mood;
    private String github;
    private String school;
    private String major;
    private String language;
    private Integer accepted_number;
    private Integer total_score;
    private Integer submission_number;
}
