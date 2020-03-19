package com.yw.ojproject.dto;

import com.yw.ojproject.bo.ProblemIOModeBo;
import com.yw.ojproject.bo.ProblemSample;
import com.yw.ojproject.entity.Problem;
import com.yw.ojproject.entity.ProblemTag;
import com.yw.ojproject.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-18 14:48
**/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemsDto {
    public ProblemsDto(Problem problem)
    {
        this.id = problem.getId();
        this.tags = new LinkedList<>();
        for(ProblemTag tmp : problem.getTags())
        {
            this.tags.add(tmp.getName());
        }
        this.created_by = new UserSimpleDto(problem.getCreate_by().getId(), problem.getCreate_by().getUsername(), null);
        this.templates = problem.getTemplate();
        this._id = problem.getId();
        this.title = problem.getTitle();
        this.description = problem.getDescription();
        this.input_description = problem.getInput_description();
        this.output_description = problem.getOutput_description();
        this.samples = JsonUtils.jsonStringToList(problem.getSamples(), List.class);
        this.hint = problem.getHint();
        this.languages = JsonUtils.jsonStringToList(problem.getLanguages(), List.class);
        this.create_time = problem.getTime();
        this.last_update_time = problem.getLast_update_time();
        this.time_limit = problem.getTime_limit();
        this.memory_limit = problem.getMemory_limit();
        this.io_mode = JsonUtils.jsonStringToObject(problem.getIo_mode(), ProblemIOModeBo.class);
        this.spj = problem.getSpj();
        this.spj_language = problem.getSpj_language();
        this.rule_type = problem.getRule_type().getDesc();
        this.difficulty = problem.getDifficulty().getDesc();
        this.source = problem.getSource();
        this.total_score = problem.getTotal_score();
        this.submission_number = problem.getSubmission_number();
        this.accepted_number = problem.getAccepted_number();
        this.statistic_info = JsonUtils.jsonStringToObject(problem.getStatistic_info(), Map.class);
        this.share_submission = problem.getShare_submission();
        this.contest = null;
        this.my_status = problem.getMy_status();
    }

    private Integer id;
    private List<String> tags;
    private UserSimpleDto created_by;
    private String templates;
    private Integer _id;
    private String title;
    private String description;
    private String input_description;
    private String output_description;
    private List<ProblemSample> samples;
    private String hint;
    private List<String> languages;
    private Date create_time;
    private Date last_update_time;
    private Integer time_limit;
    private Integer memory_limit;
    private ProblemIOModeBo io_mode;
    private Boolean spj;
    private String spj_language;
    private String rule_type;
    private String difficulty;
    private String source;
    private Integer total_score;
    private Integer submission_number;
    private Integer accepted_number;
    private Map<Integer, Integer> statistic_info;
    private Boolean share_submission;
    private String contest;
    private Integer my_status;
}
