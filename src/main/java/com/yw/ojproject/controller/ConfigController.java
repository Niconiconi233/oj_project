package com.yw.ojproject.controller;

import com.yw.ojproject.aop.SuperadminRequired;
import com.yw.ojproject.bo.HeartBeatBo;
import com.yw.ojproject.dto.*;
import com.yw.ojproject.service.ConfigServer;
import com.yw.ojproject.service.JudgeServerServer;
import com.yw.ojproject.utils.IpUtils;
import com.yw.ojproject.utils.JsonUtils;
import com.yw.ojproject.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

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

    @Autowired
    JudgeServerServer judgeServerServer;

    @Autowired
    private Environment environment;

    /**
     * @Description: 获取网页信息
     * @Param: []
     * @return: com.yw.ojproject.dto.ReturnData
     * @Author: YW
     * @Date:
     */
    @GetMapping("/website")
    public ReturnData website() throws IOException {
        Object ans = null;
        ans = JsonUtils.ResolveJsonFileToObject(environment.getProperty("confPath") + "webconfig.json");
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
    public ReturnData adminWebsite() throws IOException {
        Object ans = null;
        //尝试读取发送配置
        ans = JsonUtils.ResolveJsonFileToObject(environment.getProperty("confPath") + "webconfig.json");
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
        JsonUtils.ResolveObjectToJsonFile(websiteConfigDto, environment.getProperty("confPath") + "webconfig.json");
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
    public ReturnData adminGetSMTP() throws IOException {
        Object ans = null;
        //尝试读取发送配置
        ans = JsonUtils.ResolveJsonFileToObject(environment.getProperty("confPath") + "websmtp.json");
        return new ReturnData(null, ans);
    }

    @SuperadminRequired
    @PostMapping("/admin/smtp")
    public ReturnData adminPostSMTP(@RequestBody WebsiteSMTPDto websiteSMTPDto) throws IOException {
        JsonUtils.ResolveObjectToJsonFile(websiteSMTPDto, environment.getProperty("confPath") + "websmtp.json");
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
    public Object languages() {
        //FIXME 替换连接
        //Object result = RequestUtils.sendPostRequest("http://oj-judge:8080/language", null, "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92");
        Object result = RequestUtils.sendPostRequest("http://localhost:8081/language", null, "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92");
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
    public ReturnData dashBoardInfo() {
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
    public ReturnData versions() {
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
    public ReturnData adminGetTestCase() {
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
    public ReturnData adminDelTestCase(@RequestParam String id) {
        return configServer.delTestCase(id);
    }


    /**
     * @Description: judgerserver心跳包响应
     * @Param: [heartBeatBo, httpServletRequest]
     * @return: com.yw.ojproject.dto.ReturnData
     * @Author: YW
     * @Date:
     */
    @PostMapping("/judge_server_heartbeat")
    public ReturnData handleHeartbeat(@RequestBody HeartBeatBo heartBeatBo, HttpServletRequest httpServletRequest) throws InterruptedException {
        String token = httpServletRequest.getHeader("X-JUDGE-SERVER-TOKEN");
        if (token == null || token.compareTo("") == 0) {
            return new ReturnData("error", "Invaild Token");
        }
        String ip = IpUtils.getIpAddr(httpServletRequest);
        return judgeServerServer.handleHeartbeat(heartBeatBo, token, ip);
    }

    /**
     * @Description: 获取评测服务器列表
     * @Param: []
     * @return: com.yw.ojproject.dto.ReturnData
     * @Author: YW
     * @Date:
     */
    @SuperadminRequired
    @GetMapping("/admin/judge_server")
    public ReturnData adminGetServer() {
        return judgeServerServer.getJudgeServer();
    }

    /**
    * @Description: 管理员接口 删除judgeserver
    * @Param: [hostname]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    @SuperadminRequired
    @DeleteMapping("/admin/judge_server")
    public ReturnData adminDelServer(@RequestParam String hostname) throws InterruptedException {
        return judgeServerServer.delJudgeServer(hostname);
    }

    /**
    * @Description: 管理员接口 修改judgeserver
    * @Param: [params]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    @SuperadminRequired
    @PutMapping("/admin/judge_server")
    public ReturnData adminPutServer(@RequestBody Map<String, String> params)
    {
        Boolean disabled = false;
        if(params.get("is_disabled").compareTo("true") == 0)
        {
            disabled = true;
        }
        return judgeServerServer.putJudgeServer(params.get("id"), disabled);
    }
}
