package com.yw.ojproject.service.impl;

import com.yw.ojproject.dao.AnnouncementDao;
import com.yw.ojproject.dto.AnnouncementDto;
import com.yw.ojproject.dto.AnnouncementListDto;
import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.entity.Announcement;
import com.yw.ojproject.entity.User;
import com.yw.ojproject.service.AnnouncementServer;
import com.yw.ojproject.utils.CookieUtils;
import com.yw.ojproject.utils.JsonUtils;
import com.yw.ojproject.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-13 19:15
**/
@Service
public class AnnouncementServerImpl implements AnnouncementServer {

    @Autowired
    AnnouncementDao announcementDao;

    @Autowired
    RedisUtils redisUtils;

    @Override
    public ReturnData getAnnouncement()
    {
        List<Announcement> list = announcementDao.findAllByVisibleTrue();
        List<AnnouncementDto> res = new LinkedList<>();
        for(Announcement i : list)
        {
            res.add(new AnnouncementDto(i));
        }
        return new ReturnData(null, new AnnouncementListDto(res, res.size()));
    }

    @Override
    public ReturnData setAnnouncement(String title, String content, Boolean visible, HttpServletRequest httpServletRequest)
    {
        Cookie cookie = CookieUtils.get(httpServletRequest, "csrftoken");
        String ustr = (String)redisUtils.get(cookie.getValue());
        User u = JsonUtils.jsonStringToObject(ustr, User.class);
        Announcement announcement = new Announcement();
        announcement.setContent(content);
        announcement.setTitle(title);
        announcement.setCreate_by(u.getUsername());
        announcement.setCreate_time(new Date());
        announcement.setLast_update_time(new Date());
        announcement.setVisible(visible);
        announcementDao.save(announcement);
        return new ReturnData(null, new AnnouncementDto(announcement));
    }

    @Override
    public ReturnData delAnnouncement(String id)
    {
        announcementDao.deleteById(id);
        return new ReturnData(null, "Succeeded");
    }
}
