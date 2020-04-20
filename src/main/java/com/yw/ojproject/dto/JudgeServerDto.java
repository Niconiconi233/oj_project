package com.yw.ojproject.dto;

import com.yw.ojproject.entity.JudgeServer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
* @program: ojproject
*
* @description: 评测服务器DTO
*
* @author: YW
*
* @create: 2020-03-31 15:05
**/
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JudgeServerDto {
    public JudgeServerDto(JudgeServer judgeServer)
    {
        this.id = judgeServer.getId();
        this.status = judgeServer.status();
        this.hostname = judgeServer.getHostname();
        this.ip = judgeServer.getIp();
        this.judger_version = judgeServer.getJudger_version();
        this.cpu_core = judgeServer.getCpu_core();
        this.cpu_usage = judgeServer.getCpu();
        this.memory_usage = judgeServer.getMemory_usage();
        this.last_heartbeat = judgeServer.getHeartbeat();
        this.create_time = judgeServer.getCreate_time();
        this.task_number = judgeServer.getTasknumber();
        this.service_url = judgeServer.getService_url();
        this.is_disabled = judgeServer.getDisabled();
    }
    private String id;
    private String status;
    private String hostname;
    private String ip;
    private String judger_version;
    private Integer cpu_core;
    private Float memory_usage;
    private Float cpu_usage;
    private Date last_heartbeat;
    private Date create_time;
    private Integer task_number;
    private String service_url;
    private Boolean is_disabled;
}
