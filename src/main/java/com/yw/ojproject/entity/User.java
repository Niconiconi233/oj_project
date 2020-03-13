package com.yw.ojproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
* @program: ojproject
*
* @description: model user
*
* @author: YW
*
* @create: 2020-03-11 17:07
**/
@Entity
@Table(name = "User")
@Data
public class User {
    public User(String username, String password, String email)
    {
        this.username = username;
        this.password = password;
        this.email = email;
        this.create_time = new Date();
    }
    public User()
    {}

    @Id
    @GeneratedValue(generator = "guidGenerator")
    @GenericGenerator(name = "guidGenerator", strategy ="uuid")
    @Column(name = "ID", unique = true, nullable = true, length = 32)
    private String id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "CREATE_TIME")
    private Date create_time;

    @Column(name = "ADMIN_TYPE")
    private AdminType adminType = AdminType.REGULAR_USER;

    @Column(name = "PROBLEM_PERMISSION")
    private ProblemPermission problemPermission = ProblemPermission.NONE;

    @JsonIgnore
    @Column(name = "RESET_PASSWORD_TOKEN")
    private String reset_password_token = "";

    @JsonIgnore
    @Column(name = "RESET_PASSWORD_TOKEN_EXPR")
    private Date reset_password_token_expr;

    @JsonIgnore
    @Column(name = "AUTH_TOKEN")
    private String auth_token = "";

    @Column(name = "TWO_FACTOR_AUTH")
    private Boolean two_factor_auth = false;

    @JsonIgnore
    @Column(name = "TFA_TOKEN")
    private String tfa_token = "";

    @JsonIgnore
    @Column(name = "SESSION_KEYS")
    private String sessioin_keys = "";

    @Column(name = "OPEN_API")
    private Boolean open_api = false;

    @JsonIgnore
    @Column(name = "OPEN_API_APPKEY")
    private String open_api_key = "";

    @Column(name = "IS_DISABLED")
    private Boolean id_disabled = false;

    @Transient
    public Boolean isAdmin()
    {
        return adminType == AdminType.ADMIN;
    }

    @Transient
    public Boolean isSuperAdmin()
    {
        return adminType == AdminType.SUPER_ADMIN;
    }

    @Transient
    public Boolean isAdminRole()
    {
        return adminType == AdminType.ADMIN || adminType == AdminType.SUPER_ADMIN;
    }

    @Transient
    public Boolean canMgmtAllProblem()
    {
        return problemPermission == ProblemPermission.ALL;
    }
}
