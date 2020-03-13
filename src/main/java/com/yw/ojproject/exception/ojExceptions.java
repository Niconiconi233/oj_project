package com.yw.ojproject.exception;/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-12 15:42
**/
public class ojExceptions extends RuntimeException {
    private String code;
    private String msg;

    public ojExceptions(String code, String msg)
    {
        super(msg);
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }
}
