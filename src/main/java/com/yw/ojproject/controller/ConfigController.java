package com.yw.ojproject.controller;

import com.yw.ojproject.aop.AdminRequired;
import com.yw.ojproject.aop.LoginRequired;
import com.yw.ojproject.aop.SuperadminRequired;
import com.yw.ojproject.dto.*;
import com.yw.ojproject.service.ConfigServer;
import com.yw.ojproject.utils.JsonUtils;
import net.bytebuddy.implementation.bind.annotation.Super;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

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

    @Autowired
    ConfigServer configServer;

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
        Object ans = JsonUtils.ResolveJsonFileToObject("webconfig.json");
        return new ReturnData(null, ans);
    }

    /**
     * @Description: 管理员接口 获取网站信息
     * @Param: []
     * @return: com.yw.ojproject.dto.ReturnData
     * @Author: YW
     * @Date:
     */
    @SuperadminRequired
    @GetMapping("/admin/website")
    public ReturnData adminWebsite() {

        Object ans = JsonUtils.ResolveJsonFileToObject("webconfig.json");
        return new ReturnData(null, ans);
    }

    /**
    * @Description: 管理员接口 修改网站信息
    * @Param: [websiteConfigDto]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    @SuperadminRequired
    @PostMapping("/admin/website")
    public ReturnData adminPostWebsite(@RequestBody WebsiteConfigDto websiteConfigDto) throws IOException {
        JsonUtils.ResolveObjectToJsonFile(websiteConfigDto, "webconfig.json");
        return new ReturnData();
    }

    /**
    * @Description: 管理员接口 获取smtp信息
    * @Param: []
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    @SuperadminRequired
    @GetMapping("/admin/smtp")
    public ReturnData adminGetSMTP()
    {
        Object ans = JsonUtils.ResolveJsonFileToObject("websmtp.json");
        return new ReturnData(null, ans);
    }

    @SuperadminRequired
    @PostMapping("/admin/smtp")
    public ReturnData adminPostSMTP(@RequestBody WebsiteSMTPDto websiteSMTPDto) throws IOException {
        JsonUtils.ResolveObjectToJsonFile(websiteSMTPDto, "websmtp.json");
        return new ReturnData();

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
        Object result = restTemplate.postForObject("http://207.246.102.9:10086/language", new HttpEntity<String>(httpHeaders), Object.class);
        return result;
    }

    /**
    * @Description: 网站统计
    * @Param: []
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date: 
    */
    @GetMapping("/admin/dashboard_info")
    public ReturnData dashBoardInfo()
    {
        DashBoardInfoDto info = new DashBoardInfoDto();
        info.getEnv().put("FORCE_HTTPS", false);
        info.getEnv().put("STATIC_CDN_HOST", "");
        return new ReturnData(null, info);
    }

    /**
    * @Description: 网站动态信息
    * @Param: []
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date: 
    */
    @SuperadminRequired
    @GetMapping("/admin/versions")
    public ReturnData versions()
    {
        return new ReturnData(null, new WebSiteVersionDto());
    }


    /**
    * @Description: 获取测试用例
    * @Param: []
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    @SuperadminRequired
    @GetMapping("/admin/prune_test_case")
    public ReturnData adminGetTestCase()
    {
        return configServer.getAllTestCase();
    }

    /**
    * @Description: 管理员接口 删除测试文件
    * @Param: [id]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date: 
    */
    @SuperadminRequired
    @DeleteMapping("/admin/prune_test_case")
    public ReturnData adminDelTestCase(@RequestParam String id)
    {
        return configServer.delTestCase(id);
    }





}
