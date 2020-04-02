package com.yw.ojproject.service;

import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.entity.ProblemTag;

public interface ProblemTagServer {
    /**
    * @Description: 获取标签
    * @Param: []
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    public ReturnData getProblemTag();
    
    /**
    * @Description: 通过name获取tag
    * @Param: [name]
    * @return: com.yw.ojproject.entity.ProblemTag
    * @Author: YW
    * @Date: 
    */
    public ProblemTag findTagByName(String name);


}
