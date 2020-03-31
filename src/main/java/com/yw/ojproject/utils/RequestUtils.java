package com.yw.ojproject.utils;

import com.yw.ojproject.dto.ReturnData;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-31 22:47
**/
public class RequestUtils {

    public static ReturnData sendPostRequest(String url, Map<String, Object> params, String token){
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Judge-Server-Token", token);
        HttpMethod method = HttpMethod.POST;
        // 以什么方式提交，自行选择，一般使用json，或者表单
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        //将请求头部和参数合成一个请求
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params, headers);
        //执行HTTP请求，将返回的结构使用Response类格式化
        ResponseEntity<ReturnData> response = client.exchange(url, method, requestEntity, ReturnData.class);

        return response.getBody();
    }
}
