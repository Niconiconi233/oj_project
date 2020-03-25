package com.yw.ojproject.controller;

import com.yw.ojproject.aop.SuperadminRequired;
import com.yw.ojproject.dto.DashBoardInfoDto;
import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.dto.WebSiteVersionDto;
import com.yw.ojproject.dto.WebsiteConfigDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-14 14:53
**/

@RestController
@RequestMapping("/api")
public class ConfigController {

    /**
    * @Description: 获取网页信息
    * @Param: []
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date: 
    */
    @GetMapping("/website")
    public ReturnData website()
    {
        return new ReturnData(null, new WebsiteConfigDto());
    }

    /**
    * @Description: 获取语言环境变量
    * @Param: []
    * @return: java.lang.Object
    * @Author: YW
    * @Date: 
    */
    @GetMapping("/languages")
    public Object languages()
    {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Judge-Server-Token", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92");
        httpHeaders.add("Content-Type","application/json");
        Object result = restTemplate.postForObject("http://localhost:5000/language", new HttpEntity<String>(httpHeaders), Object.class);
        return result;
    }

    @GetMapping("/admin/dashboard_info")
    public ReturnData dashBoardInfo()
    {
        DashBoardInfoDto info = new DashBoardInfoDto();
        info.getEnv().put("FORCE_HTTPS", false);
        info.getEnv().put("STATIC_CDN_HOST", "");
        return new ReturnData(null, info);
    }

    @SuperadminRequired
    @GetMapping("/admin/versions")
    public ReturnData versions()
    {
        return new ReturnData(null, new WebSiteVersionDto());
    }


}
