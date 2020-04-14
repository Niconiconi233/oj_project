package com.yw.ojproject.scheduled;

import com.yw.ojproject.service.JudgeServerServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
* @program: ojproject
*
* @description:  定时任务相关
*
* @author: YW
*
* @create: 2020-04-13 14:29
**/
@Slf4j
@Async("taskExecutor")
@Component
public class ScheduleJob {
    public final static long ONE_Minute =  60 * 1000;

    public final static long test_minute = 10 * 1000;

    @Autowired
    JudgeServerServer judgeServerServer;


    @Scheduled(fixedRate = ONE_Minute)
    public void writeServers()
    {
        log.info("flush cache");
        judgeServerServer.flushServers();
    }
}
