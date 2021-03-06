package com.yw.ojproject.entity;

import com.yw.ojproject.bo.UserProfileBo;
import com.yw.ojproject.dto.VoProblems;
import com.yw.ojproject.utils.JsonUtils;
import lombok.Data;

import javax.persistence.*;
import java.util.HashMap;

/**
* @program: ojproject
*
* @description: user profile
*
* @author: YW
*
* @create: 2020-03-11 18:13
**/
@Data
@Entity
@Table(name = "UserProfile")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, length = 32)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private User user;

    @Column(name = "ACM_PROBLEMS_STATUS")
    private String acm_problems_status = JsonUtils.objectToJson(new HashMap<String, VoProblems> ());

    @Column(name = "IO_PROBLEMS_STATUS")
    private String io_problems_status = JsonUtils.objectToJson(new HashMap<String, VoProblems> ());

    @Column(name = "REAL_NAME")
    private String real_name;

    @Column(name = "AVATAR")
    private String avator = "/default.png";

    @Column(name = "BLOG")
    private String blog;

    @Column(name = "MOOD")
    private String mood;

    @Column(name = "GITHUB")
    private String github;

    @Column(name = "SCHOOLD")
    private String school;

    @Column(name = "MAJOR")
    private String major;

    @Column(name = "LANGUAGE")
    private String language;

    @Column(name = "ACCEPED_NUMBER")
    private Integer acceptnumber = 0;

    @Column(name = "TOTAL_SCORE")
    private Integer totalscore = 0;

    @Column(name = "SUBMISSION_NUMBER")
    private Integer submissionnumber = 0;

    @Transient
    private Boolean hasChanged = false;

    @Transient
    public boolean hasChanged()
    {
        return hasChanged;
    }

    @Transient
    public void updateProfile(UserProfileBo userProfileBo)
    {
        if(userProfileBo.getReal_name() != null)
        {
            this.setReal_name(userProfileBo.getReal_name());
        }
        if(userProfileBo.getMood() != null)
        {
            this.setMood(userProfileBo.getMood());
        }
        if(userProfileBo.getMajor() != null)
        {
            this.setMajor(userProfileBo.getMajor());
        }
        if(userProfileBo.getBlog() != null)
        {
            this.setBlog(userProfileBo.getBlog());
        }
        if(userProfileBo.getSchool() != null)
        {
            this.setSchool(userProfileBo.getSchool());
        }
        if(userProfileBo.getGithub() != null)
        {
            this.setGithub(userProfileBo.getGithub());
        }
        if(userProfileBo.getLanguage() != null)
        {
            this.setLanguage(userProfileBo.getLanguage());
        }
    }
}
