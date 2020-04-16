package com.yw.ojproject.service;

import com.yw.ojproject.bo.UserProfileBo;
import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.entity.UserProfile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserProfileServer extends BaseServer<UserProfile>  {

    /**
    * @Description: 获取用户信息
    * @Param: [httpServletRequest]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    public ReturnData userProfile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws InterruptedException;

    /**
    * @Description: 修改信息
    * @Param: [userProfileBo]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    public ReturnData modUserProfile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, UserProfileBo userProfileBo) throws InterruptedException;


    /**
    * @Description: 异步刷新数据库
    * @Param: []
    * @return: void
    * @Author: YW
    * @Date:
    */
    public void flushProfileCache();
}
