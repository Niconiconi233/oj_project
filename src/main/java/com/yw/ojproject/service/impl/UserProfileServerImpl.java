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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-11 20:05
**/
@Slf4j
@Service
public class UserProfileServerImpl extends BaseServerImpl<UserProfile> implements UserProfileServer {

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    UserDao userDao;

    static Lock lock;

    //保存登录的用户信息
    static Map<String, UserProfile> users;
    static
    {
         lock = new ReentrantLock();
        users = new HashMap<>();
    }

    private UserProfileDao userProfileDao;

    public UserProfileServerImpl(UserProfileDao userProfileDao)
    {
        this.dao = userProfileDao;
        this.userProfileDao = userProfileDao;
    }

    @Override
    public ReturnData userProfile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws InterruptedException {
        Cookie profileCookie = CookieUtils.get(httpServletRequest, "_pid");
        Cookie userCookie = CookieUtils.get(httpServletRequest, "csrftoken");
        //尝试从内存中获取
        if(profileCookie != null && users.containsKey(profileCookie.getValue()))
        {
            if(lock.tryLock(5, TimeUnit.SECONDS))
            {
                try {
                    return new ReturnData(null, new UserProfileDto(users.get(profileCookie.getValue())));
                }catch (Exception e)
                {
                    log.warn("catch Exceptions: " + e.getMessage());
                    throw e;
                }finally {
                    lock.unlock();
                }
            }
        }
        //尝试从redis中获取
        if(profileCookie != null)
        {
            String profile = (String)redisUtils.get(profileCookie.getValue());
            //意外情况下登录成功但是没有信息在redis中
            if(profile != null)
            {
                //重新设置到缓存和map中
                UserProfile userProfile = JsonUtils.jsonStringToObject(profile, UserProfile.class);
                redisUtils.set(profileCookie.getValue(), JsonUtils.objectToJson(userProfile), 432000);
                lock.lock();
                try {
                    users.put(profileCookie.getValue(), userProfile);
                }catch (Exception e)
                {
                    log.warn("catch Exceptions: " + e.getMessage());
                }finally {
                    lock.unlock();
                }
                return new ReturnData(null, new UserProfileDto(userProfile));
            }
        }
        //第一次登录后获取或者缓存和redis中的失效
        if(userCookie != null)
        {
            String ustr = (String)redisUtils.get(userCookie.getValue());
            User u = JsonUtils.jsonStringToObject(ustr, User.class);
            UserProfile profile = userProfileDao.findByUser(u);
            String _pid = UUID.randomUUID().toString().replace("-", "");
            CookieUtils.set(httpServletResponse, "_pid", _pid, 432000);
            redisUtils.set(_pid, JsonUtils.objectToJson(profile), 432000);
            lock.lock();
            try {
                users.put(_pid, profile);
            }catch (Exception e)
            {
                log.warn("catch Exceptions: " + e.getMessage());
                throw e;
            }finally {
                lock.unlock();
            }
            return new ReturnData(null, new UserProfileDto(profile));
        }
        //未知错误，可能是未登录情况下获取数据
        return new ReturnData("error", "Please Relogin");
    }

    @Override
    public ReturnData modUserProfile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, UserProfileBo userProfileBo) throws InterruptedException {
        Cookie profileCookie = CookieUtils.get(httpServletRequest, "_pid");
        Cookie userCookie = CookieUtils.get(httpServletRequest, "csrftoken");
        UserProfile userProfile = null;
        Long last_time = null;
        if(profileCookie != null && users.containsKey(profileCookie.getValue()))
        {
            lock.lock();
            try
            {
                userProfile = users.get(profileCookie.getValue());
                userProfile.updateProfile(userProfileBo);
                userProfile.setHasChanged(true);
                users.put(profileCookie.getValue(), userProfile);
            }catch (Exception e)
            {
                log.warn("catch Exceptions: " + e.getMessage());
                throw e;
            }finally {
                lock.unlock();
            }
            last_time = redisUtils.getExpire(userCookie.getValue());
            redisUtils.set(profileCookie.getValue(), JsonUtils.objectToJson(userProfile), last_time);
            String ustr = (String)redisUtils.get(userCookie.getValue());
            User u = JsonUtils.jsonStringToObject(ustr, User.class);
            u.setReal_name(userProfileBo.getReal_name());
            userDao.save(u);
            redisUtils.set(userCookie.getValue(), JsonUtils.objectToJson(u), last_time);
            return new ReturnData(null, new UserProfileDto(userProfile));
        }else
        {
            String pstr = (String)redisUtils.get(profileCookie.getValue());
            User u = JsonUtils.jsonStringToObject((String)redisUtils.get(userCookie.getValue()), User.class);
            last_time = redisUtils.getExpire(userCookie.getValue());
            if(pstr == null)
            {
                userProfile = userProfileDao.findByUser(u);
                userProfile.updateProfile(userProfileBo);
                userProfile.setHasChanged(true);
            }else
            {
                userProfile = JsonUtils.jsonStringToObject(pstr, UserProfile.class);
                userProfile.updateProfile(userProfileBo);
                userProfile.setHasChanged(true);
            }
            u.setReal_name(userProfileBo.getReal_name());
            userDao.save(u);
            redisUtils.set(userCookie.getValue(), JsonUtils.objectToJson(u), last_time);
            redisUtils.set(profileCookie.getValue(), JsonUtils.objectToJson(userProfile), last_time);
            lock.lock();
            try{
                users.put(profileCookie.getValue(), userProfile);
            }catch (Exception e)
            {
                log.warn("catch Exceptions: " + e.getMessage());
                throw e;
            }finally {
                lock.unlock();
            }
            return new ReturnData(null, new UserProfileDto(userProfile));
        }
    }

    public static UserProfile getProfileByPid(String _pid)
    {
        lock.lock();
        try
        {
            UserProfile ans = users.get(_pid);
            return ans;
        }catch (Exception e)
        {
            log.warn("catch Exceptionis: " + e.getMessage());
            throw e;
        }finally {
            lock.unlock();
        }
    }

    @Override
    public void flushProfileCache()
    {
        lock.lock();
        try
        {
            for(Map.Entry<String, UserProfile> entry : users.entrySet())
            {
                if(entry.getValue().getHasChanged())
                {
                    UserProfile tmp = entry.getValue();
                    userProfileDao.save(tmp);
                    tmp.setHasChanged(false);
                    users.put(entry.getKey(), tmp);
                }
            }
        }catch (Exception e)
        {
            log.warn("catch Exceptions: " + e.getMessage());
            throw e;
        }finally {
            lock.unlock();
        }
    }

    public static void delProfileByPid(String _pid)
    {
        if(!users.containsKey(_pid))
        {
            return;
        }
        lock.lock();
        try
        {
            users.remove(_pid);
        }catch (Exception e)
        {
            log.warn("catch Exceptionis: " + e.getMessage());
            throw e;
        }finally {
            lock.unlock();
        }
    }

    public static void putProfileByPid(String _pid, UserProfile userProfile)
    {
        lock.lock();
        try
        {
            users.put(_pid, userProfile);
        }catch (Exception e)
        {
            log.warn("catch Exceptionis: " + e.getMessage());
            throw e;
        }finally {
            lock.unlock();
        }
    }
}
