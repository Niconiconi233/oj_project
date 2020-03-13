package com.yw.ojproject.service;

import com.yw.ojproject.dto.ReturnData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserServer {
    /**
    * @Description: 注册接口
    * @Param: [username, email]
    * @return: com.yw.ojproject.entity.User
    * @Author: YW
    * @Date: 
    */
    public ReturnData userRegister(String username, String password, String email);

    /**
    * @Description: 登录接口
    * @Param: [username, password]
    * @return: com.yw.ojproject.dto.SimpleDto
    * @Author: YW
    * @Date:
    */
    public ReturnData userLogin(String username, String password, HttpServletResponse httpResponse);

    /**
    * @Description: 登出
    * @Param: [httpResponse]
    * @return: com.yw.ojproject.dto.SimpleDto
    * @Author: YW
    * @Date:
    */
    public ReturnData userLogout(HttpServletRequest httpServletRequest);

    /**
    * @Description: 检查用户名邮箱
    * @Param: [email, username]
    * @return: com.yw.ojproject.dto.SimpleDto
    * @Author: YW
    * @Date:
    */
    public ReturnData checkUsernameOrEmail(String email, String username);


    /**
    * @Description: 修改邮箱
    * @Param: [password, old_email, new_email, httpServletRequest]
    * @return: com.yw.ojproject.dto.SimpleDto
    * @Author: YW
    * @Date:
    */
    public ReturnData changeEmail(String password, String old_email, String new_email, HttpServletRequest httpServletRequest);

    /**
    * @Description: 修改密码
    * @Param: [old_password, new_password, httpServletRequest]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    public ReturnData changePassword(String old_password, String new_password, HttpServletRequest httpServletRequest);
}
