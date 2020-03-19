package com.yw.ojproject.entity;

import com.yw.ojproject.bo.*;
import com.yw.ojproject.utils.JsonUtils;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
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
    public Problem()
    {
        /*this.title;
        this.description;
        this.input_description;
        this.output_description;
        this.test_case_id;
        this.template;
        this.create_by;
        this.time_limit;
        this.memory_limit;
        this.spj_language;
        this.spj_code;
        this.spj_version;
        this.rule_type;
        this.tags;
        this.source;*/
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@GenericGenerator(name = "guidGenerator", strategy ="uuid")
    @Column(name = "ID", unique = true, nullable = true, length = 32)
    private Integer id;

    //@Column(name = "contest")
    //private Contest contest;

    @Column(name = "IS_PUBLIC")
    private Integer is_public = 1;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "INPUT_DESCRIPTION")
    private String input_description;

    @Column(name = "OUTPUT_DESCRIPTION")
    private String output_description;

    @Column(name = "SAMPLES")
    private String samples = JsonUtils.listToJsonString(new LinkedList<ProblemSample>());

    @Column(name = "TEST_CASE_ID")
    private String test_case_id;

    @Column(name = "TEST_CASE_SCORE")
    private String test_case_score = JsonUtils.listToJsonString(new LinkedList<ProblemCaseScore>());

    @Column(name = "HINT")
    private String hint = null;

    @Column(name = "LANGUAGES")
    private String languages = JsonUtils.listToJsonString(new LinkedList<String>());

    @Column(name = "TEMPLATE")
    private String template;

    @Column(name = "CREATE_TIME")
    private Date time = new Date();

    @Column(name = "LAST_UPDATE_TIME")
    private Date last_update_time = new Date();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CREATE_BY", referencedColumnName = "ID")
    private User create_by;

    @Column(name = "TIME_LIMIT")
    private Integer time_limit;

    @Column(name = "MEMORY_LIMIT")
    private Integer memory_limit;

    @Column(name = "IO_MODE")
    private String io_mode = JsonUtils.objectToJson(ProblemIOModeBo.default_io_mode());

    @Column(name = "SPJ")
    private Boolean spj = false;

    @Column(name = "SPJ_LANGUAGE")
    private String spj_language;

    @Column(name = "SPJ_CODE")
    private String spj_code;

    @Column(name = "SPJ_VERSION")
    private String spj_version;

    @Column(name = "SPJ_COMPILE_OK")
    private Boolean spj_compile_ok = false;

    @Column(name = "RULE_TYPE")
    private ProblemRuleType rule_type;

    @Column(name = "VISIBLE")
    private Integer visible = 1;

    @Column(name = "DIFFICULTY")
    private ProblemDifficulty difficulty;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "TAG_ID", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
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
    private String statistic_info = JsonUtils.objectToJson(new HashMap<Integer, Integer>());

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

}
