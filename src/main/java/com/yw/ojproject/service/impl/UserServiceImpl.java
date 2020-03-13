package com.yw.ojproject.service.impl;

import com.yw.ojproject.dao.UserDao;
import com.yw.ojproject.dao.UserProfileDao;
import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.dto.UsernameEmailStatusDto;
import com.yw.ojproject.entity.User;
import com.yw.ojproject.entity.UserProfile;
import com.yw.ojproject.service.UserService;
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
* @description: user impl
*
* @author: YW
*
* @create: 2020-03-11 17:51
**/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;
    @Autowired
    UserProfileDao userProfileDao;
    @Autowired
    RedisUtils redisUtils;

    @Override
    public ReturnData userRegister(String username, String password, String email)
    {
        //TODO check 验证码
        if(userDao.countByUsername(username) >= 1)
        {
            return new ReturnData("error", "Username already exists");
        }
        if(userDao.countByEmail(email) >= 1)
        {
            return new ReturnData("error", "Email already exists");
        }
        User u = new User(username, password, email);
        UserProfile p = new UserProfile();
        p.setUser(u);
        userDao.save(u);
        userProfileDao.save(p);
        return new ReturnData("","Succeeded");
    }

    @Override
    public ReturnData userLogin(String username, String password, HttpServletResponse response)
    {
        User u = userDao.findByUsernameAndPassword(username, password);
        if(u == null)
        {
            return new ReturnData("Invaild username or password", "");
        }
        if(u.getId_disabled())
        {
            return new ReturnData("Your account is been disabled", "");
        }
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String user_json = JsonUtils.objectToJson(u);
        redisUtils.set(uuid, user_json, 432000);
        CookieUtils.set(response, "csrftoken", uuid, 432000);
        return new ReturnData("", "Succeeded");
    }

    @Override
    public ReturnData userLogout(HttpServletRequest httpServletRequest)
    {
        Cookie cookie = CookieUtils.get(httpServletRequest, "csrftoken");
        redisUtils.del(cookie.getValue());
        cookie = CookieUtils.get(httpServletRequest, "_pid");
        redisUtils.del(cookie.getValue());
        return new ReturnData("", "succeeded");
    }

    @Override
    public ReturnData checkUsernameOrEmail(String email, String username)
    {
        if(email.compareTo("") == 0)
        {
            Integer count = userDao.countByUsername(username);
            if(count >= 1) {
                return new ReturnData("", new UsernameEmailStatusDto(true, false));
            }else {
                return new ReturnData("", new UsernameEmailStatusDto(false, false));
            }
        }else
        {
            Integer count = userDao.countByEmail(email);
            if(count >= 1) {
                return new ReturnData("", new UsernameEmailStatusDto(false, true));
            }else {
                return new ReturnData("", new UsernameEmailStatusDto(false, false));
            }
        }
    }

    @Override
    public ReturnData changeEmail(String password, String old_email, String new_email, HttpServletRequest httpServletRequest)
    {
        Cookie cookie = CookieUtils.get(httpServletRequest, "csrftoken");
        String ustr = (String)redisUtils.get(cookie.getValue());
        User u = JsonUtils.jsonStringToObject(ustr, User.class);
        if(u.getPassword().compareTo(password) != 0)
        {
            return new ReturnData("error", "Wrong password");
        }
        u.setEmail(new_email);
        userDao.save(u);
        redisUtils.set(cookie.getValue(), JsonUtils.objectToJson(u), 432000);
        return new ReturnData("", "Succeeded");
    }

    @Override
    public ReturnData changePassword(String old_password, String new_password, HttpServletRequest httpServletRequest)
    {
        Cookie cookie = CookieUtils.get(httpServletRequest, "csrftoken");
        String ustr = (String)redisUtils.get(cookie.getValue());
        User u = JsonUtils.jsonStringToObject(ustr, User.class);
        if(u.getPassword().compareTo(old_password) != 0)
        {
            return new ReturnData("error", "Invalid old password");
        }
        u.setPassword(new_password);
        userDao.save(u);
        redisUtils.set(cookie.getValue(), JsonUtils.objectToJson(u), 432000);
        return new ReturnData("", "Succeeded");
    }
}
