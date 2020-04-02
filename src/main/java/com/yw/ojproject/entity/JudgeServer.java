package com.yw.ojproject.entity;

import com.yw.ojproject.bo.HeartBeatBo;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
* @program: ojproject
*
* @description: 编译服务器
*
* @author: YW
*
* @create: 2020-03-14 16:06
**/
@Entity
@Data
@Table(name = "JudgeServer")
public class JudgeServer {
    public JudgeServer(){}

    public JudgeServer(HeartBeatBo heartBeatBo, String ip, String token)
    {
        this.hostname = heartBeatBo.getHostname();
        this.ip = ip;
        this.judger_version = heartBeatBo.getJudger_version();
        this.cpu_core = heartBeatBo.getCpu_core();
        this.memory_usage = heartBeatBo.getMemory();
        this.heartbeat = new Date();
        this.create_time = new Date();
        this.tasknumber = 0;
        this.service_url = heartBeatBo.getService_url();
        this.disabled = false;
        this.token = token;
    }

    @Id
    @GeneratedValue(generator = "guidGenerator")
    @GenericGenerator(name = "guidGenerator", strategy = "uuid")
    @Column(name = "ID", unique = true, nullable = false, length = 32)
    private String id;

    @Column(name = "HOSTNAME", length = 32)
    private String hostname;

    @Column(name = "IP", length = 32)
    private String ip;

    @Column(name = "JUDGER_VERSION", length = 32)
    private String judger_version;

    @Column(name = "CPU_CORE")
    private Integer cpu_core;

    @Column(name = "CPU_USAGE")
    private Float cpu;

    @Column(name = "MEMORY_USAGE")
    private Float memory_usage;

    @Column(name = "LAST_HEARTBEAT")
    private Date heartbeat;

    @Column(name = "CREATE_TIIME")
    private Date create_time;

    @Column(name = "TASK_NUMBER")
    private Integer tasknumber;

    @Column(name = "SERVICE_URL", length = 64)
    private String service_url;

    @Column(name = "IS_DISABLED")
    private Boolean disabled;

    @Column(name = "TOKEN")
    private String token;

    @Transient
    public String status()
    {
        Date now = new Date();
        Long res = (now.getTime() - this.heartbeat.getTime())/1000;
        if(res > 6) {
            return "abnormal";
        }
        return "normal";
    }

    @Transient
    public void updateServer(HeartBeatBo heartBeatBo)
    {
        this.hostname = heartBeatBo.getHostname();
        this.judger_version = heartBeatBo.getJudger_version();
        this.cpu_core = heartBeatBo.getCpu_core();
        this.cpu = heartBeatBo.getCpu();
        this.memory_usage = heartBeatBo.getMemory();
        this.heartbeat = new Date();
        this.service_url = heartBeatBo.getService_url();
    }

}
