package com.yw.ojproject.service.impl;

import com.yw.ojproject.bo.HeartBeatBo;
import com.yw.ojproject.dao.JudgeServerDao;
import com.yw.ojproject.dto.JudgeServerDto;
import com.yw.ojproject.dto.JudgeServerListDto;
import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.entity.JudgeServer;
import com.yw.ojproject.service.JudgeServerServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
* @create: 2020-03-14 16:28
**/
@Slf4j
@Service
public class JudgeServerServerImpl implements JudgeServerServer {

    @Autowired
    JudgeServerDao judgeServerDao;

    @Autowired
    private Environment environment;

    private Lock lock = new ReentrantLock();

    //保存的是活跃的服务器
    static Map<String, JudgeServer> servers;
    static
    {
        servers = new HashMap<>();
    }

    @Override
    public ReturnData handleHeartbeat(HeartBeatBo heartBeatBo, String token, String ip) throws InterruptedException {
        //判断是否在map缓存中
        if(servers.containsKey(heartBeatBo.getHostname()))
        {
            //尝试获取锁，如果没获取什么也不做，等待下一次
            if(lock.tryLock(5, TimeUnit.SECONDS)) {
                try {
                    JudgeServer judgeServer = servers.get(heartBeatBo.getHostname());
                    judgeServer.updateServer(heartBeatBo);
                    servers.put(heartBeatBo.getHostname(), judgeServer);
                    return new ReturnData();
                }catch (Exception e)
                {
                    log.warn("throw Exception" + e.getMessage());
                    throw e;
                }finally {
                    lock.unlock();
                }
            }else {
                log.warn("get lock failed");
            }
        }else {
            JudgeServer judgeServer = judgeServerDao.findByHostname(heartBeatBo.getHostname());
            if (judgeServer == null)//新server
            {
                judgeServer = new JudgeServer(heartBeatBo, ip, token);
                judgeServerDao.save(judgeServer);
            }
            if(lock.tryLock(5, TimeUnit.SECONDS)) {
                try {
                    servers.put(heartBeatBo.getHostname(), judgeServer);
                    return new ReturnData();
                }catch (Exception e)
                {
                    log.warn("throw Exception: " + e.getMessage());
                    throw e;
                }finally {
                    lock.unlock();
                }
            }else
            {
                log.warn("get lock failed");
            }
        }
        //未知错误
        log.warn("unkown error");
        return null;
    }


    @Override
    public void flushServers()
    {
        for(JudgeServer server : servers.values())
        {
            Date now = new Date();
            Long res = (now.getTime() - server.getHeartbeat().getTime())/1000;
            //只保存活跃的
            if(res <= 10)
            {
                judgeServerDao.save(server);
            }else
            {
                //清除不活跃的连接，释放资源
                lock.lock();
                try
                {
                    servers.remove(server.getHostname());
                }catch (Exception e)
                {
                    log.warn("catch Exceptions: " +e.getMessage());
                    throw e;
                }finally {
                    lock.unlock();
                }
            }
        }
    }

    @Override
    public ReturnData getJudgeServer()
    {
        //刷新缓存
        flushServers();
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
    public ReturnData delJudgeServer(String hostname) throws InterruptedException {
        judgeServerDao.deleteByHostname(hostname);
        if(servers.containsKey(hostname))
        {
            lock.lock();
            try{
                servers.remove(hostname);
            }catch (Exception e)
            {
                log.warn("catch Exception: " + e.getMessage());
                throw e;
            }finally {
                lock.unlock();
            }
        }
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
        if(servers.containsKey(judgeServer.getHostname()))
        {
            lock.lock();
            try {
                servers.put(judgeServer.getHostname(), judgeServer);
            }catch (Exception e)
            {
                log.warn("catch Exception: " + e.getMessage());
                throw e;
            }finally {
                lock.unlock();
            }
        }
        return new ReturnData();
    }

    @Override
    public JudgeServer chooseJudgeServer()
    {
        for(JudgeServer server : servers.values())
        {
            if(server.status().compareTo("normal") == 0)
            {
                if(server.getTasknumber() <= server.getCpu_core() * 2)
                {
                    server.setTasknumber(server.getTasknumber() + 1);
                    return server;
                }
            }
        }
        log.warn("can't find server");
        return null;
    }

    @Override
    public void releaseJudgeServer(JudgeServer judgeServer)
    {
        judgeServer.setTasknumber(judgeServer.getTasknumber() - 1);
        lock.lock();
        try {
            servers.put(judgeServer.getHostname(), judgeServer);
        }catch (Exception e)
        {
            log.warn("catch Exception: " + e.getMessage());
            throw e;
        }finally {
            lock.unlock();
        }
    }
}
