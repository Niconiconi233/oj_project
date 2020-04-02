package com.yw.ojproject.service;

import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.entity.Submission;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-04-02 16:01
**/
public interface SubmissionServer extends BaseServer<Submission> {
    
    /**
    * @Description: 保存submission
    * @Param: [params]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date: 
    */
    public ReturnData postSubmission(Map<String, Object> params, HttpServletRequest httpServletRequest);
    
    /**
    * @Description: 获取submission
    * @Param: [id]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date: 
    */
    public ReturnData getSubmission(String id, HttpServletRequest httpServletRequest);
    
    /**
    * @Description: 修改submission
    * @Param: [id, can_shared]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date: 
    */
    public ReturnData putSubmission(String id, Boolean can_shared);

    /**
    * @Description: 通过问题查询记录
    * @Param: [id]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    public ReturnData getSubmissionByProblemId(Integer id, Integer offset, Integer size);

    /**
    * @Description: 通过用户查询记录
    * @Param: [offset, size, httpServletRequest]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    public ReturnData getSubmissionByUser(Integer offset, Integer size, HttpServletRequest httpServletRequest);
}
