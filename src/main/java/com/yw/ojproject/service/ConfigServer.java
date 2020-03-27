package com.yw.ojproject.service;

import com.yw.ojproject.dto.ReturnData;

public interface ConfigServer {

    /**
    * @Description: 获取全部测试用例文件
    * @Param: []
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date: 
    */
    public ReturnData getAllTestCase();
    
    /**
    * @Description: 删除测试用例
    * @Param: [id]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date: 
    */
    public ReturnData delTestCase(String id);
}
