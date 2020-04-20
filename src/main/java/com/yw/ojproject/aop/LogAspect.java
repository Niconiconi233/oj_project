package com.yw.ojproject.aop;

import com.yw.ojproject.exception.ojExceptions;
import com.yw.ojproject.utils.CookieUtils;
import com.yw.ojproject.utils.RedisUtils;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
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
* @description: 登录判断
*
* @author: YW
*
* @create: 2020-03-11 22:35
**/

@Aspect
@Component
@Log4j2
public class LogAspect {
    @Autowired
    RedisUtils redisUtils;

     Logger logger = LoggerFactory.getLogger(LogAspect.class);
    /**
     * 功能描述: 拦截对这个包下所有方法的访问
     *
     * @param:[]
     * @return:void
     **/
    @Pointcut(value = "@annotation(com.yw.ojproject.aop.LoginRequired)")
    public void loginCheck() {
    }

    // 前置通知
    @Before("loginCheck()")
    public void loginBefore(JoinPoint joinPoint) {

        // 我们从请求的上下文中获取request，记录请求的内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = attributes.getRequest();
        Cookie cookie = CookieUtils.get(request, "csrftoken");

        if(cookie == null)
        {
            logger.warn("cookie中查不到token");
            throw new ojExceptions("001", "找不到token");
        }

        if(!redisUtils.hasKey(cookie.getValue()))
        {
            logger.warn("redis中找不到token");
            throw new ojExceptions("002", "redis中找不到token");
        }
        //logger.info("请求路径 : " + request.getRequestURL());
        //logger.info("请求方式 : " + request.getMethod());
        //logger.info("方法名 : " + joinPoint.getSignature().getName());
        //logger.info("类路径 : " + joinPoint.getSignature().getDeclaringTypeName());
        //logger.info("参数 : " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(returning = "object", pointcut = "loginCheck()")
    public void doAfterReturning(Object object) {
        logger.info("方法的返回值 : " + object);
        //System.out.println("方法的返回值 : " + object);
    }

    // 方法发生异常时执行该方法
    @AfterThrowing(throwing = "e",pointcut = "loginCheck()")
    public void throwsExecute(JoinPoint joinPoint, Exception e) {
        logger.info("方法执行异常 : " + e.getMessage());
        //System.err.println("方法执行异常 : " + e.getMessage());
    }

    // 后置通知
    @After("loginCheck()")
    public void afterInform() {
        logger.info("后置通知结束");
        //System.out.println("后置通知结束");
    }

    // 环绕通知
    /*@Around("loginLog()")
    public Object surroundInform(ProceedingJoinPoint proceedingJoinPoint) {
        log.info("环绕通知开始...");
        Object o =  proceedingJoinPoint.proceed();
        log.info("方法环绕proceed，结果是 :" + o);
        return o;
    }*/
}
