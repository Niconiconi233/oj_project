package com.yw.ojproject.dto;

import com.yw.ojproject.bo.JudgeStatus;
import com.yw.ojproject.entity.Submission;
import com.yw.ojproject.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-04-02 19:32
**/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionDto {
    public SubmissionDto(Submission submission)
    {
        this.id = submission.getId();
        this.create_time = submission.getCtime();
        this.user_id = submission.getUser().getId();
        this.username = submission.getUser().getUsername();
        this.code = submission.getCode();
        this.result = submission.getResult();
        this.info = JsonUtils.jsonStringToObject(submission.getInfo(), ReturnData.class);
        this.language = submission.getLanguage();
        this.statistic_info = JsonUtils.jsonStringToObject(submission.getStatistic_info(), Map.class);
        this.ip = submission.getIp();
        this.problem = submission.getProblem().getId();
    }
    private String id;
    private Date create_time;
    private Integer user_id;
    private String username;
    private String code;
    private Integer result;
    private ReturnData info;
    private String language;
    private Map<String, Object> statistic_info;
    private String ip;
    private Object contest = null;
    private Integer problem;
    private Boolean can_unshare;
}
