package com.yw.ojproject.service.impl;

import com.yw.ojproject.bo.UserProfileBo;
import com.yw.ojproject.dao.UserDao;
import com.yw.ojproject.dao.UserProfileDao;
import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.dto.UserProfileDto;
import com.yw.ojproject.entity.User;
import com.yw.ojproject.entity.UserProfile;
import com.yw.ojproject.service.UserProfileServer;
import com.yw.ojproject.utils.CookieUtils;
import com.yw.ojproject.utils.JsonUtils;
import com.yw.ojproject.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-11 20:05
**/
@Service
public class UserProfileServerImpl extends BaseServerImpl<UserProfile> implements UserProfileServer {

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    UserDao userDao;

    private UserProfileDao userProfileDao;

    public UserProfileServerImpl(UserProfileDao userProfileDao)
    {
        this.dao = userProfileDao;
        this.userProfileDao = userProfileDao;
    }

    @Override
    public void addSubmissionNumber(UserProfile userProfile)
    {
        userProfile.setSubmissionnumber(userProfile.getSubmissionnumber() + 1);
        userProfileDao.save(userProfile);
    }

    @Override
    public void addScore(UserProfile userProfile, Integer this_time_score, Integer last_time_score)
    {
        userProfile.setTotalscore(userProfile.getTotalscore() - last_time_score + this_time_score);
        userProfileDao.save(userProfile);
    }

    @Override
    public void addAcceptedProblemNumber(UserProfile userProfile)
    {
        userProfile.setAcceptnumber(userProfile.getAcceptnumber() + 1);
        userProfileDao.save(userProfile);
    }

    @Override
    public ReturnData userProfile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
    {
        Cookie cookie = CookieUtils.get(httpServletRequest, "_pid");
        if(cookie != null)
        {
            String profile = (String)redisUtils.get(cookie.getValue());
            return new ReturnData(null, new UserProfileDto(JsonUtils.jsonStringToObject(profile, UserProfile.class)));
        }
        cookie = CookieUtils.get(httpServletRequest, "csrftoken");
        if(cookie != null)
        {
            String ustr = (String)redisUtils.get(cookie.getValue());
            User u = JsonUtils.jsonStringToObject(ustr, User.class);
            UserProfile profile = userProfileDao.findByUser(u);
            String _pid = UUID.randomUUID().toString().replace("-", "");
            CookieUtils.set(httpServletResponse, "_pid", _pid, 432000);
            redisUtils.set(_pid, JsonUtils.objectToJson(profile), 432000);
            return new ReturnData(null, new UserProfileDto(profile));
        }
        return new ReturnData("error", "system error");
    }

    @Override
    public ReturnData modUserProfile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, UserProfileBo userProfileBo)
    {
        //数据库中的是准确数据
        Cookie cookie = CookieUtils.get(httpServletRequest, "csrftoken");
        UserProfile userProfile = null;
        Long last_time = null;
        if(cookie != null)
        {
            String ustr = (String)redisUtils.get(cookie.getValue());
            User u = JsonUtils.jsonStringToObject(ustr, User.class);
            u.setReal_name(userProfileBo.getReal_name());
            userDao.save(u);
            userProfile = userProfileDao.findByUser(u);
            userProfile.updateProfile(userProfileBo);
            userProfileDao.save(userProfile);
            //刷新user缓存，不刷新profile的
            last_time = redisUtils.getExpire(cookie.getValue());
            redisUtils.set(cookie.getValue(), JsonUtils.objectToJson(u), last_time);
        }
        cookie = CookieUtils.get(httpServletRequest, "_pid");
        if(cookie != null && userProfile != null && last_time != null)
        {
            redisUtils.set(cookie.getValue(), JsonUtils.objectToJson(userProfile), last_time);
        }
        return new ReturnData(null, userProfile);
    }

}
