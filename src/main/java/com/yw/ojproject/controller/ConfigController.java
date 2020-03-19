package com.yw.ojproject.controller;

import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.dto.WebsiteConfigDto;
import com.yw.ojproject.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @GetMapping("/website")
    ReturnData website()
    {
        return new ReturnData(null, new WebsiteConfigDto());
    }

    @GetMapping("/languages")
    Object languages()
    {
        //向judger server请求
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Judge-Server-Token", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92");
        httpHeaders.add("Content-Type","application/json");
        Object result = restTemplate.postForObject("http://localhost:5000/language", new HttpEntity<String>(httpHeaders), Object.class);
        return result;
    }
}
