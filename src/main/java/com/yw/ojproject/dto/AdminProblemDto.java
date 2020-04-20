package com.yw.ojproject.dto;

import com.yw.ojproject.bo.ProblemCaseScoreBo;
import com.yw.ojproject.enums.ProblemDifficulty;
import com.yw.ojproject.bo.ProblemIOModeBo;
import com.yw.ojproject.bo.ProblemSampleBo;
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
* @description: 管理员获取问题DTO
*
* @author: YW
*
* @create: 2020-03-24 19:35
**/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminProblemDto {

    public AdminProblemDto(Problem problem)
    {
        this.id = problem.getId();
        this.tags = new LinkedList<>();
        for(ProblemTag tag : problem.getTags())
        {
            this.tags.add(tag.getName());
        }
        this.is_public = problem.getIs_public();
        this.created_by = new UserSimpleDto(problem.getCreate_by().getId(), problem.getCreate_by().getUsername(), null);
        this.templates = problem.getTemplates();
        this.title = problem.getTitle();
        this.description = problem.getDescription();
        this.input_description = problem.getInput_description();
        this.output_description = problem.getOutput_description();
        this.samples = JsonUtils.jsonStringToList(problem.getSamples(), ProblemSampleBo.class);
        this.test_case_id = problem.getTest_case_id();
        this.test_case_score = JsonUtils.jsonStringToList(problem.getTest_case_score(), ProblemCaseScoreBo.class);
        this.hint = problem.getHint();
        this.languages = JsonUtils.jsonStringToList(problem.getLanguages(), String.class);
        this.create_time = problem.getTime();
        this.last_update_time = problem.getLast_update_time();
        this.time_limit = problem.getTime_limit();
        this.memory_limit = problem.getMemory_limit();
        this.io_mode = JsonUtils.jsonStringToObject(problem.getIo_mode(), ProblemIOModeBo.class);
        this.spj = problem.getSpj();
        this.spj_language = problem.getSpj_language();
        this.spj_code = problem.getSpj_code();
        this.spj_version = problem.getSpj_version();
        this.spj_compile_ok = problem.getSpj_compile_ok();
        this.rule_type = problem.getRule_type().getDesc();
        this.visible = problem.getVisible();
        this.difficulty = ProblemDifficulty.getDifficultyEnumByCode(problem.getDifficulty()).getDesc();
        this.source = problem.getSource();
        this.submission_number = problem.getSubmission_number();
        this.accepted_number = problem.getAccepted_number();
        this.total_score = problem.getTotal_score();
        this.statistic_info = JsonUtils.jsonStringToObject(problem.getStatistic_info(), Map.class);
        this.share_submission = problem.getShare_submission();

    }

    private Integer id;
    private List<String> tags;
    private Boolean is_public;
    private UserSimpleDto created_by;
    private String templates;
    private String title;
    private String description;
    private String input_description;
    private String output_description;
    private List<ProblemSampleBo> samples;
    private String test_case_id;
    private List<ProblemCaseScoreBo> test_case_score;
    private String hint;
    private List<String> languages;
    private Date create_time;
    private Date last_update_time;
    private Integer time_limit;
    private Integer memory_limit;
    private ProblemIOModeBo io_mode;
    private Boolean spj;
    private String spj_language;
    private String spj_code;
    private String spj_version;
    private Boolean spj_compile_ok;
    private String rule_type;
    private Boolean visible;
    private String difficulty;
    private String source;
    private Integer total_score;
    private Integer submission_number;
    private Integer accepted_number;
    private Map<Integer, Integer> statistic_info;
    private Boolean share_submission;
    private String contest = null;
}
