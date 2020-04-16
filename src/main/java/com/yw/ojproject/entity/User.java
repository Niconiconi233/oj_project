package com.yw.ojproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yw.ojproject.bo.AdminType;
import com.yw.ojproject.bo.ProblemPermission;
import com.yw.ojproject.dto.UserTotalDto;
import lombok.Data;

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
        this.ctime = new Date();
    }
    public User()
    {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, length = 32)
    private Integer id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "REAL_NAME")
    private String real_name;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "CREATE_TIME")
    private Date ctime;

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

    @Transient
    public void updateUser(UserTotalDto userTotalDto)
    {
        this.username = userTotalDto.getUsername();
        this.email = userTotalDto.getEmail();
        if(userTotalDto.getAdmin_type().compareTo(AdminType.ADMIN.getDesc()) == 0)
        {
            this.adminType = AdminType.ADMIN;
        }else if(userTotalDto.getAdmin_type().compareTo(AdminType.SUPER_ADMIN.getDesc()) == 0)
        {
            this.adminType = AdminType.SUPER_ADMIN;
        }else
        {
            this.adminType = AdminType.REGULAR_USER;
        }
        if(userTotalDto.getProblem_permission().compareTo(ProblemPermission.ALL.getDesc()) == 0)
        {
            this.problemPermission = ProblemPermission.ALL;
        }else if(userTotalDto.getProblem_permission().compareTo(ProblemPermission.OWN.getDesc()) == 0)
        {
            this.problemPermission = ProblemPermission.OWN;
        }else
        {
            this.problemPermission = ProblemPermission.NONE;
        }
        this.real_name = userTotalDto.getReal_name();
        this.two_factor_auth = userTotalDto.getTwo_factor_auth();
        this.open_api = userTotalDto.getOpen_api();
        this.id_disabled = userTotalDto.getIs_disabled();
        if(userTotalDto.getPassword().compareTo("") != 0) {
            this.password = userTotalDto.getPassword();
        }
    }
}
