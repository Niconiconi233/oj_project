package com.yw.ojproject.service;

import com.yw.ojproject.dto.ReturnData;

import javax.servlet.http.HttpServletRequest;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-13 19:13
**/
public interface AnnouncementServer {

    /**
    * @Description: 获取公告
    * @Param: []
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    ReturnData getAnnouncement();

    /**
    * @Description: 设置公告
    * @Param: []
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    public ReturnData setAnnouncement(String title, String content, Boolean visible, HttpServletRequest httpServletRequest);
}
