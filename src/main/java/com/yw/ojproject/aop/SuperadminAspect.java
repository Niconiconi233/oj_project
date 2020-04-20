package com.yw.ojproject.aop;

import com.yw.ojproject.entity.User;
import com.yw.ojproject.exception.ojExceptions;
import com.yw.ojproject.utils.CookieUtils;
import com.yw.ojproject.utils.JsonUtils;
import com.yw.ojproject.utils.RedisUtils;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
* @program: ojproject
*
* @description: 超级管理员判断
*
* @author: YW
*
* @create: 2020-03-13 19:24
**/
@Aspect
@Component
@Log4j2
public class SuperadminAspect {

    @Autowired
    RedisUtils redisUtils;

    Logger logger = LoggerFactory.getLogger(SuperadminAspect.class);

    /**
     * 功能描述: 拦截对这个包下所有方法的访问
     *
     * @param:[]
     * @return:void
     **/
    @Pointcut(value = "@annotation(com.yw.ojproject.aop.SuperadminRequired)")
    public void superadminCheck() {
    }

    // 前置通知
    @Before("superadminCheck()")
    public void loginBefore(JoinPoint joinPoint) {

        // 我们从请求的上下文中获取request，记录请求的内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = attributes.getRequest();
        Cookie cookie = CookieUtils.get(request, "csrftoken");

        if(cookie == null)
        {
            logger.warn("cookie中查不到token");
            throw new ojExceptions("001", "未登陆");
        }

        if(!redisUtils.hasKey(cookie.getValue()))
        {
            logger.warn("redis中找不到token");
            throw new ojExceptions("002", "已过期");
        }

        String ustr = (String)redisUtils.get(cookie.getValue());
        User user = JsonUtils.jsonStringToObject(ustr, User.class);
        if(!user.isSuperAdmin())
        {
            logger.warn("非超级管理员");
            throw new ojExceptions("004", "非超级管理员");
        }
    }
}
