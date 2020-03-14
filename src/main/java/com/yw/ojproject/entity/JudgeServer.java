package com.yw.ojproject.entity;

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
    @Id
    @GeneratedValue(generator = "guidGenerator")
    @GenericGenerator(name = "guidGenerator", strategy = "uuid")
    @Column(name = "ID", unique = true, nullable = true, length = 32)
    private String id;

    @Column(name = "HOSTNAME", length = 32)
    private String hostname;

    @Column(name = "IP", length = 32)
    private String ip;

    @Column(name = "JUDGER_VERSION", length = 32)
    private String judger_version;

    @Column(name = "CPU_CORE")
    private Integer cpu_core;

    @Column(name = "MEMORY_USAGE")
    private Float memory_usage;

    @Column(name = "LAST_HEARTBEAT")
    private Date last_heartbeat;

    @Column(name = "CREATE_TIIME")
    private Date create_time;

    @Column(name = "TASK_NUMBER")
    private Integer tasknumber;

    @Column(name = "SERVICE_URL", length = 64)
    private String service_url;

    @Column(name = "IS_DISABLED")
    private Boolean disabled;

    @Transient
    String status()
    {
        Date now = new Date();
        if(now.getTime() - this.last_heartbeat.getTime() > 6) {
            return "abnormal";
        }
        return "normal";

    }

}
