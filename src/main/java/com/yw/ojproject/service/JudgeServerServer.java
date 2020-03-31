package com.yw.ojproject.service;

import com.yw.ojproject.bo.HeartBeatBo;
import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.entity.JudgeServer;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-14 16:27
**/
public interface JudgeServerServer {

    /**
    * @Description: 心跳包更新
    * @Param: [heartBeatBo, token, ip]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    public ReturnData handleHeartbeat(HeartBeatBo heartBeatBo, String token, String ip);

    /**
    * @Description: 获取全部评测服务器
    * @Param: []
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    public ReturnData getJudgeServer();
    
    
    /**
    * @Description: 删除评测服务器
    * @Param: [hostname]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date: 
    */
    public ReturnData delJudgeServer(String hostname);

    /**
    * @Description: 修改评测服务器
    * @Param: [id, is_disabled]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    public ReturnData putJudgeServer(String id, Boolean is_disabled);


    /**
    * @Description: 选择一个judgerserver
    * @Param: []
    * @return: com.yw.ojproject.entity.JudgeServer
    * @Author: YW
    * @Date:
    */
    public JudgeServer chooseJudgeServer();

    /**
    * @Description: 释放一个judgeserver
    * @Param: [judgeServer]
    * @return: void
    * @Author: YW
    * @Date:
    */
    public void releaseJudgeServer(JudgeServer judgeServer);
}
