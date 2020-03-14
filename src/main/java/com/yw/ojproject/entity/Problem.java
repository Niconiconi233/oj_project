package com.yw.ojproject.entity;

import com.yw.ojproject.bo.ProblemDifficulty;
import com.yw.ojproject.bo.ProblemIOMode;
import com.yw.ojproject.bo.ProblemIOModeBo;
import com.yw.ojproject.utils.JsonUtils;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
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
    @Id
    @GeneratedValue(generator = "guidGenerator")
    @GenericGenerator(name = "guidGenerator", strategy ="uuid")
    @Column(name = "ID", unique = true, nullable = true, length = 32)
    private String id;

    //@Column(name = "contest")
    //private Contest contest;

    @Column(name = "IS_PUBLIC")
    private Boolean is_public;

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

    @Column(name = "TEST_CASE_SCORE")
    private String test_case_score;

    @Column(name = "HINT")
    private String hint;

    @Column(name = "LANGUAGES")
    private String languages;

    @Column(name = "TEMPLATE")
    private String template;

    @Column(name = "CREATE_TIME")
    private Date create_time;

    @Column(name = "LAST_UPDATE_TIME")
    private Date last_update_time;

    @Column(name = "CREATE_BY")
    private String create_by;

    @Column(name = "TIME_LIMIT")
    private Integer time_limit;

    @Column(name = "MEMORY_LIMIT")
    private Integer memory_limit;

    @Column(name = "IO_MODE")
    private String io_mode = JsonUtils.objectToJson(ProblemIOModeBo.default_io_mode());

    @Column(name = "SPJ")
    private Boolean spj;

    @Column(name = "SPJ_LANGUAGE")
    private String spj_language;

    @Column(name = "SPJ_CODE")
    private String spj_code;

    @Column(name = "SPJ_VERSION")
    private String spj_version;

    @Column(name = "SPJ_COMPILE_OK")
    private Boolean spj_compile_ok;

    @Column(name = "RULE_TYPE")
    private String rule_type;

    @Column(name = "VISIBLE")
    private Boolean visible;

    @Column(name = "DIFFICULTY")
    private ProblemDifficulty difficulty;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "TAG_ID", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private List<ProblemTag> tags;

    @Column(name = "SOURCE")
    private String source;

    @Column(name = "TOTAL_SCORE")
    private Integer total_score;

    @Column(name = "SUBMISSION_NUMBER")
    private Integer submission_number;

    @Column(name = "ACCEPTED_NUMBER")
    private Integer accepted_number;

    @Column(name = "STATISTIC_INFO")
    private String statistic_info;

    @Column(name = "SHARE_SUBMISSION")
    private Boolean share_submission;

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
