package com.yw.ojproject.dto;

import com.yw.ojproject.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
* @program: ojproject
*
* @description: 用户详细信息
*
* @author: YW
*
* @create: 2020-03-19 15:00
**/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTotalDto {
    public UserTotalDto(User user)
    {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.admin_type = user.getAdminType().desc();
        this.problem_permission = user.getProblemPermission().desc();
        this.create_time = user.getCreate_time();
        this.last_login = new Date();
        this.two_factor_auth = user.getTwo_factor_auth();
        this.open_api = user.getOpen_api();
        this.is_disabled = user.getId_disabled();
    }
    private Integer id;
    private String username;
    private String email;
    private String admin_type;
    private String problem_permission;
    private Date create_time;
    private Date last_login;
    private Boolean two_factor_auth;
    private Boolean open_api;
    private Boolean is_disabled;
}
