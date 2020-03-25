package com.yw.ojproject.service;

import com.yw.ojproject.bo.AnnouncementBo;
import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.entity.Announcement;

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
public interface AnnouncementServer extends BaseServer<Announcement> {

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
    public ReturnData setAnnouncement(AnnouncementBo announcementBo, HttpServletRequest httpServletRequest);
    
    /**
    * @Description: 删除公告
    * @Param: [id]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date: 
    */
    public ReturnData delAnnouncement(Long id);
    
    /**
    * @Description: 修改公告
    * @Param: [announcementBo]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date: 
    */
    public ReturnData putAnnouncement(AnnouncementBo announcementBo);
}
