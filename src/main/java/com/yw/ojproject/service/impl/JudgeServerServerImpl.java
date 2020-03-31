package com.yw.ojproject.service.impl;

import com.yw.ojproject.bo.HeartBeatBo;
import com.yw.ojproject.dao.JudgeServerDao;
import com.yw.ojproject.dto.JudgeServerDto;
import com.yw.ojproject.dto.JudgeServerListDto;
import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.entity.JudgeServer;
import com.yw.ojproject.service.JudgeServerServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-14 16:28
**/
@Service
public class JudgeServerServerImpl implements JudgeServerServer {

    @Autowired
    JudgeServerDao judgeServerDao;

    @Autowired
    private Environment environment;

    @Override
    public ReturnData handleHeartbeat(HeartBeatBo heartBeatBo, String token, String ip)
    {
        JudgeServer judgeServer = judgeServerDao.findByHostname(heartBeatBo.getHostname());
        if(judgeServer == null)//新server
        {
            judgeServer = new JudgeServer(heartBeatBo, ip, token);
            judgeServerDao.save(judgeServer);
            return new ReturnData();
        }else
        {
         judgeServer.updateServer(heartBeatBo);
         judgeServerDao.save(judgeServer);
         return new ReturnData();
        }
    }

    @Override
    public ReturnData getJudgeServer()
    {
        //TODO 排序
        List<JudgeServer> servers = judgeServerDao.findAll();
        List<JudgeServerDto> res = new LinkedList<>();
        for(JudgeServer j : servers)
        {
            res.add(new JudgeServerDto(j));
        }
        JudgeServerListDto ans = new JudgeServerListDto(environment.getProperty("token"), res);
        return new ReturnData(null, ans);
    }


    @Override
    @Transactional
    public ReturnData delJudgeServer(String hostname)
    {
        judgeServerDao.deleteByHostname(hostname);
        return new ReturnData(null, "success");
    }

    @Override
    public ReturnData putJudgeServer(String id, Boolean is_disbaled)
    {
        JudgeServer judgeServer = judgeServerDao.findById(id).orElse(null);
        if(judgeServer == null)
        {
            return new ReturnData();
        }
        judgeServer.setDisabled(is_disbaled);
        judgeServerDao.save(judgeServer);
        return new ReturnData();
    }

    @Override
    public JudgeServer chooseJudgeServer()
    {
        List<JudgeServer> res = judgeServerDao.findAllByDisabledFalseOrderByTasknumberAsc();
        for(JudgeServer j : res)
        {
            if(j.status().compareTo("normal") == 0)
            {
                if(j.getTasknumber() <= j.getCpu_core() * 2)
                {
                    j.setTasknumber(j.getTasknumber() + 1);
                    judgeServerDao.save(j);
                    return j;
                }
            }
        }
        return null;
    }

    @Override
    public void releaseJudgeServer(JudgeServer judgeServer)
    {
        judgeServer.setTasknumber(judgeServer.getTasknumber() - 1);
        judgeServerDao.save(judgeServer);
    }

}
