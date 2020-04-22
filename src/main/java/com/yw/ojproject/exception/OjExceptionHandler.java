package com.yw.ojproject.exception;

import com.yw.ojproject.dto.ReturnData;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.security.auth.login.AccountException;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-12 15:40
**/
@ControllerAdvice
public class OjExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public ReturnData handle(RuntimeException e)
    {
        logger.error("系统错误", e);
        return new ReturnData("error", e.getMessage());
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseBody
    public ReturnData handleUnauthorized(UnauthorizedException e)
    {
        logger.info("未授权登录");
        return new ReturnData("login-required", "Please login in first");
    }

    @ExceptionHandler(value = UnknownAccountException.class)
    @ResponseBody
    public ReturnData handleUnkownAccount(UnknownAccountException e)
    {
        logger.info("未知账户");
        return new ReturnData("error", "未知账户");
    }

    @ExceptionHandler(value = IncorrectCredentialsException.class)
    @ResponseBody
    public ReturnData handleIncorrect(IncorrectCredentialsException e)
    {
        logger.info("密码错误");
        return new ReturnData("error", "密码错误");
    }

    @ExceptionHandler(value = LockedAccountException.class)
    @ResponseBody
    public ReturnData handleLocked(LockedAccountException e)
    {
        logger.info("账户被封禁");
        return new ReturnData("error", "账户已被封禁");
    }

    @ExceptionHandler(value = AuthenticationException.class)
    @ResponseBody
    public ReturnData handleAuthentication(AuthenticationException e)
    {
        logger.info("用户名或密码不正确");
        return new ReturnData("error", "用户名或密码不正确");
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ReturnData handle(Exception e)
    {
        logger.error("系统错误", e);
        return new ReturnData("error", e.getMessage());
    }
}
