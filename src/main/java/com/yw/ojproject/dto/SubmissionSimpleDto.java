package com.yw.ojproject.dto;

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
* @create: 2020-04-02 21:11
**/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionSimpleDto {
    public SubmissionSimpleDto(Submission submission)
    {
        this.id = submission.getId();
        this.problem = submission.getProblem().getId().toString();
        this.show_link = true;
        this.create_time = submission.getCtime();
        this.user_id = submission.getUser().getId();
        this.username = submission.getUsername();
        this.result = submission.getResult();
        this.language = submission.getLanguage();
        this.shared = submission.getShared();
        this.statistic_info = JsonUtils.jsonStringToObject(submission.getStatistic_info(), Map.class);
    }
    private String id;
    private String problem;
    private Boolean show_link;
    private Date create_time;
    private Integer user_id;
    private String username;
    private Integer result;
    private String language;
    private Boolean shared;
    private Map<String, Object> statistic_info;
}
