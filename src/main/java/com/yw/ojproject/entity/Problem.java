package com.yw.ojproject.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yw.ojproject.bo.ProblemBo;
import com.yw.ojproject.bo.ProblemDifficulty;
import com.yw.ojproject.bo.ProblemRuleType;
import com.yw.ojproject.dto.AdminProblemDto;
import com.yw.ojproject.utils.JsonUtils;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-14 18:56
**/
@Entity
@Data
@Table(name = "Problem")
public class Problem {
    public Problem(){}

    public Problem(ProblemBo problemBo) {
        this.id = problemBo.get_id();
        this.title = problemBo.getTitle();
        this.description = problemBo.getDescription();
        this.input_description = problemBo.getInput_description();
        this.output_description = problemBo.getOutput_description();
        this.time_limit = problemBo.getTime_limit();
        this.memory_limit = problemBo.getMemory_limit();
        if (problemBo.getDifficulty().compareTo(ProblemDifficulty.HIGH.getDesc()) == 0) {
            this.difficulty = ProblemDifficulty.HIGH.getCode();
        } else if (problemBo.getDifficulty().compareTo(ProblemDifficulty.LOW.getDesc()) == 0) {
            this.difficulty = ProblemDifficulty.LOW.getCode();
        } else {
            this.difficulty = ProblemDifficulty.MID.getCode();
        }
        this.visible = problemBo.getVisible();
        this.share_submission = problemBo.getShare_submission();
        this.spj = problemBo.getSpj();
        this.spj_language = problemBo.getSpj_language();
        this.spj_code = problemBo.getSpj_code();
        this.spj_compile_ok = problemBo.getSpj_compile_ok();
        this.test_case_id = problemBo.getTest_case_id();
        if (problemBo.getRule_type().compareTo(ProblemRuleType.IO.getDesc()) == 0)
        {
            this.rule_type = ProblemRuleType.IO;
        }else
        {
            this.rule_type = ProblemRuleType.ACM;
        }
        this.hint = problemBo.getHint();
        this.source = problemBo.getSource();
        this.spj_version = problemBo.getSpj_version();
        this.total_score = problemBo.getTotal_score();
        this.test_case_score = JsonUtils.listToJsonString(problemBo.getTest_case_score());
        this.templates = JsonUtils.objectToJson(problemBo.getTemplate());
    }

    @Id
    @Column(name = "ID", unique = true, nullable = false, length = 32)
    private Integer id;

    //@Column(name = "contest")
    //private Contest contest;

    @Column(name = "IS_PUBLIC")
    private Boolean is_public = true;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "INPUT_DESCRIPTION")
    private String input_description;

    @Column(name = "OUTPUT_DESCRIPTION")
    private String output_description;

    @Column(name = "SAMPLES")
    private String samples;

    @Column(name = "TEST_CASE_ID")
    private String test_case_id;

    @Column(name = "TEST_CASE_SCORE", length = 512)
    private String test_case_score;

    @Column(name = "HINT", length = 2048)
    private String hint;

    @Column(name = "LANGUAGES")
    private String languages;

    @Column(name = "TEMPLATE", length = 512)
    private String templates;

    @Column(name = "CREATE_TIME")
    private Date time = new Date();

    @Column(name = "LAST_UPDATE_TIME")
    private Date last_update_time = new Date();

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "CREATE_BY", referencedColumnName = "ID")
    private User create_by;

    @Column(name = "TIME_LIMIT")
    private Integer time_limit;

    @Column(name = "MEMORY_LIMIT")
    private Integer memory_limit;

    @Column(name = "IO_MODE")
    private String io_mode;

    @Column(name = "SPJ")
    private Boolean spj;

    @Column(name = "SPJ_LANGUAGE")
    private String spj_language;

    @Column(name = "SPJ_CODE", length = 4096)
    private String spj_code;

    @Column(name = "SPJ_VERSION")
    private String spj_version;

    @Column(name = "SPJ_COMPILE_OK")
    private Boolean spj_compile_ok;

    @Column(name = "RULE_TYPE")
    private ProblemRuleType rule_type;

    @Column(name = "VISIBLE")
    private Boolean visible;

    @Column(name = "DIFFICULTY")
    private Integer difficulty;

    @JSONField(serialize = false)
    @ManyToMany(cascade = {CascadeType.PERSIST}, targetEntity = ProblemTag.class, fetch = FetchType.EAGER)
    @JoinTable(name = "PROBLEM_TAG",
            joinColumns=@JoinColumn(name="PRO_ID", referencedColumnName="ID"),
            inverseJoinColumns=@JoinColumn(name="TAG_ID", referencedColumnName="ID"))
    private List<ProblemTag> tags;

    @Column(name = "SOURCE")
    private String source;

    @Column(name = "TOTAL_SCORE")
    private Integer total_score = 0;

    @Column(name = "SUBMISSION_NUMBER")
    private Integer submission_number = 0;

    @Column(name = "ACCEPTED_NUMBER")
    private Integer accepted_number = 0;

    @Column(name = "STATISTIC_INFO")
    private String statistic_info = JsonUtils.objectToJson(new HashMap<String, Integer>());

    @Column(name = "SHARE_SUBMISSION")
    private Boolean share_submission = false;

    @Transient
    private Integer my_status = null;

    @Transient
    public void add_submission_number()
    {
        this.submission_number = this.submission_number + 1;
    }

    @Transient
    public void add_ac_number()
    {
        this.accepted_number = this.accepted_number + 1;
    }

    @Transient
    public void modProblem(AdminProblemDto adminProblemDto)
    {
        this.title = adminProblemDto.getTitle();
        this.description = adminProblemDto.getDescription();
        this.is_public = adminProblemDto.getIs_public();
        this.templates = adminProblemDto.getTemplates();
        this.samples = JsonUtils.listToJsonString(adminProblemDto.getSamples());
        this.input_description = adminProblemDto.getInput_description();
        this.output_description = adminProblemDto.getOutput_description();
        this.time_limit = adminProblemDto.getTime_limit();
        this.languages = JsonUtils.listToJsonString(adminProblemDto.getLanguages());
        this.memory_limit = adminProblemDto.getMemory_limit();
        this.last_update_time = new Date();
        this.io_mode = JsonUtils.objectToJson(adminProblemDto.getIo_mode());
        if (adminProblemDto.getDifficulty().compareTo(ProblemDifficulty.HIGH.getDesc()) == 0) {
            this.difficulty = ProblemDifficulty.HIGH.getCode();
        } else if (adminProblemDto.getDifficulty().compareTo(ProblemDifficulty.LOW.getDesc()) == 0) {
            this.difficulty = ProblemDifficulty.LOW.getCode();
        } else {
            this.difficulty = ProblemDifficulty.MID.getCode();
        }
        this.visible = adminProblemDto.getVisible();
        this.share_submission = adminProblemDto.getShare_submission();
        this.spj = adminProblemDto.getSpj();
        this.spj_language = adminProblemDto.getSpj_language();
        this.spj_code = adminProblemDto.getSpj_code();
        this.spj_compile_ok = adminProblemDto.getSpj_compile_ok();
        this.test_case_id = adminProblemDto.getTest_case_id();
        this.hint = adminProblemDto.getHint();
        this.source = adminProblemDto.getSource();
        this.spj_version = adminProblemDto.getSpj_version();
        this.total_score = adminProblemDto.getTotal_score();
        this.test_case_score = JsonUtils.listToJsonString(adminProblemDto.getTest_case_score());
        this.source = adminProblemDto.getSource();
        //this.total_score = adminProblemDto.getTotal_score();
        //this.submission_number = adminProblemDto.getSubmission_number();
        //this.accepted_number = adminProblemDto.getAccepted_number();
        //this.statistic_info = JsonUtils.objectToJson(adminProblemDto.getStatistic_info());
    }

}
