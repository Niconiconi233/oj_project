package com.yw.ojproject.dao;

import com.yw.ojproject.entity.JudgeServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface JudgeServerDao extends JpaSpecificationExecutor<JudgeServer>, JpaRepository<JudgeServer, String> {
    /**
    * @Description: 获取编译服务器
    * @Param: []
    * @return: java.util.List<com.yw.ojproject.entity.JudgeServer>
    * @Author: YW
    * @Date:
    */
    List<JudgeServer> findAllByDisabledFalseOrderByTasknumberAsc();
    
    /**
    * @Description: 通过hostname查找
    * @Param: [hostname]
    * @return: com.yw.ojproject.entity.JudgeServer
    * @Author: YW
    * @Date: 
    */
    JudgeServer findByHostname(String hostname);

    /**
    * @Description: 查找全部评测服务器
    * @Param: []
    * @return: java.util.List<com.yw.ojproject.entity.JudgeServer>
    * @Author: YW
    * @Date: 
    */
    //List<JudgeServer> findAllOrderByHeartbeatDesc();
    
    
    /**
    * @Description: 通过hostname删除
    * @Param: [hostname]
    * @return: void
    * @Author: YW
    * @Date: 
    */
    void deleteByHostname(String hostname);
}
