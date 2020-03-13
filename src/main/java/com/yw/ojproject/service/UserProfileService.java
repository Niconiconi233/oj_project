package com.yw.ojproject.service;

import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.entity.UserProfile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserProfileService extends BaseServer<UserProfile>  {
    /**
    * @Description: 增加提交数目
    * @Param: [userProfile]
    * @return: void
    * @Author: YW
    * @Date:
    */
    public void addSubmissionNumber(UserProfile userProfile);

    /**
    * @Description: 计算总分时，应先减掉上次该题所得分数， 然后再加上本次所得分数
    * @Param: [userProfile]
    * @return: void
    * @Author: YW
    * @Date:
    */
    public void addScore(UserProfile userProfile, Integer this_time_score, Integer last_time_score);
    
    /**
    * @Description: 增加ac数
    * @Param: [userProfile]
    * @return: void
    * @Author: YW
    * @Date: 
    */
    public void addAcceptedProblemNumber(UserProfile userProfile);

    /**
    * @Description: 获取用户信息
    * @Param: [httpServletRequest]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    public ReturnData userProfile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
    

}
