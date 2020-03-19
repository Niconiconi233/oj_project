package com.yw.ojproject.service;

import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.entity.Problem;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ProblemServer extends BaseServer<Problem> {

    /**
    * @Description: 随机获取一题
    * @Param: []
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    public ReturnData pickOne();


    /**
    * @Description: 通过id获取详情
    * @Param: [id]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    public ReturnData findByID(Integer id, HttpServletRequest httpServletRequest);

    /**
    * @Description: 添加信息
    * @Param: [param, httpServletRequest]
    * @return: void
    * @Author: YW
    * @Date: 
    */
    public void _add_problem_status(Problem param, HttpServletRequest httpServletRequest);

    /**
    * @Description: 添加信息
    * @Param: [param, httpServletRequest]
    * @return: void
    * @Author: YW
    * @Date: 
    */
    public void _add_problem_status(List<Problem> param, HttpServletRequest httpServletRequest);
    
    /**
    * @Description: 管理员接口 查找问题
    * @Param: [id]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date: 
    */
    public ReturnData findById(Integer id);
    
    /**
    * @Description: 上传test_case
    * @Param: [spj, file]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date: 
    */
    public ReturnData uploadTestCase(Boolean spj, MultipartFile file);
}