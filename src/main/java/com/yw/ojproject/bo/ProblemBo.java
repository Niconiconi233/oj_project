package com.yw.ojproject.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
* @program: ojproject
*
* @description: 问题BO
*
* @author: YW
*
* @create: 2020-03-22 00:06
**/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemBo {

    private Integer _id;

    private String title;

    private String description;

    private String input_description;

    private String output_description;

    private Integer time_limit;

    private Integer memory_limit;

    private String difficulty;

    private Boolean visible;

    private Boolean share_submission;

    private List<String> tags;

    private List<String> languages;

    private Map<String, String> template;

    private List<ProblemSampleBo> samples;

    private Boolean spj;

    private String spj_language;

    private String spj_code;

    private Boolean spj_compile_ok;

    private String test_case_id;

    private List<Object> test_case_score;

    private String rule_type;

    private String hint;

    private String source;

    private ProblemIOModeBo io_mode;

    private String spj_version;

    private Integer total_score = 100;
}
