package com.yw.ojproject.entity;

import com.yw.ojproject.bo.JudgeStatus;
import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.utils.JsonUtils;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;

/**
* @program: ojproject
*
* @description: 答案提交
*
* @author: YW
*
* @create: 2020-03-11 18:53
**/
@Entity
@Data
@Table(name = "Submission")
public class Submission {
    public Submission()
    {}
    public Submission(User user, Problem problem, String language, String code, String ip)
    {
        this.problem = problem;
        this.ctime = new Date();
        this.user = user;
        this.username = user.getUsername();
        this.code = code;
        this.language = language;
        this.ip = ip;
    }

    @Id
    @GeneratedValue(generator = "guidGenerator")
    @GenericGenerator(name = "guidGenerator", strategy ="uuid")
    @Column(name = "ID", unique = true, nullable = false, length = 32)
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROBLEM_ID", referencedColumnName = "ID")
    private Problem problem;

    @Column(name = "CREATE_TIME")
    private Date ctime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private User user;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "CODE")
    private String code;

    @Column(name = "RESULT")
    private Integer result = JudgeStatus.PENDING.getCode();

    @Column(name = "INFO")
    private String info = JsonUtils.objectToJson(new ReturnData());

    @Column(name = "LANGUAGE")
    private String language;

    @Column(name = "SHARED")
    private Boolean shared = false;

    @Column(name = "STATISTIC_INFO")
    private String statistic_info = JsonUtils.objectToJson(new HashMap<String, Object>());

    @Column(name = "IP")
    private String ip;

    @Transient
    public Boolean check_user_permission(User user)
    {
        if(this.user.getId().equals(user.getId()) || user.isAdmin() || user.isSuperAdmin())
        {
            return true;
        }
        return false;
    }
}
