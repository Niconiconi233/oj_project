package com.yw.ojproject.exception;

import com.yw.ojproject.dto.ReturnData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

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
        if(e instanceof ojExceptions)
        {
            logger.error("业务错误", e);
            ojExceptions o = (ojExceptions)e;
            return new ReturnData("error", e.getMessage());
        }
        logger.error("系统错误", e);
        return new ReturnData("error", e.getMessage());
    }


    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ReturnData handle(Exception e)
    {
        logger.error("系统错误", e);
        return new ReturnData("error", e.getMessage());
    }
}
