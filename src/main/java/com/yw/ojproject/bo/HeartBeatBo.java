package com.yw.ojproject.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @program: ojproject
*
* @description: 心跳包BO
*
* @author: YW
*
* @create: 2020-03-30 15:32
**/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeartBeatBo {
    private String hostname;
    private Float cpu;
    private Integer cpu_core;
    private Float memory;
    private String judger_version;
    private String actioin;
    private String service_url;
}
