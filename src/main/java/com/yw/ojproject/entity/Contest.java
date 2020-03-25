package com.yw.ojproject.entity;

import com.yw.ojproject.bo.ContestRuleType;
import com.yw.ojproject.bo.ContestStatus;
import com.yw.ojproject.bo.ContestType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-14 18:59
**/
//@Data
//@Entity
//@Table(name = "Contest")
public class Contest {
    @Id
    @GeneratedValue(generator = "guidGenerator")
    @GenericGenerator(name = "guidGenerator", strategy ="uuid")
    @Column(name = "ID", unique = true, nullable = true, length = 32)
    private String id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "REAL_TIME_RANK")
    private Boolean real_time_rank;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "RULE_TYPE")
    private ContestRuleType rule_type;

    @Column(name = "START_TIME")
    private Date start_time;

    @Column(name = "END_TIME")
    private Date end_time;

    @Column(name = "CREATE_TIME")
    private Date create_time;

    @Column(name = "LAST_UPDATE_TIME")
    private Date last_update_time;

    @Column(name = "CREATE_BY")
    private String create_by;

    @Column(name = "VISIBLE")
    private Boolean visible;

    @Column(name = "ALLOW_IP_RANGE")
    private String allow_ip_range;

    @Transient
    public ContestStatus status()
    {
        Date now = new Date();
        Integer start = this.start_time.compareTo(now);
        if(start == 1)
        {
            //没有开始
            return ContestStatus.CONTEST_NOT_START;
        }
        Integer end = this.end_time.compareTo(now);
        if(end == -1)
        {
            //已经结束
            return ContestStatus.CONTEST_END;
        }
        //正在进行
        return ContestStatus.CONTEST_UNDERWAY;
    }

    @Transient
    public ContestType contest_type()
    {
        if(this.password.compareTo("") == 0) {
            return ContestType.PASSWORD_PROTECTED_CONTEST;
        }
        return ContestType.PUBLIC;
    }

    @Transient
    public Boolean problem_details_permission(User user)
    {
        return this.rule_type == ContestRuleType.ACM || this.status() == ContestStatus.CONTEST_END || user.isAdmin() || user.isSuperAdmin();
    }
}
