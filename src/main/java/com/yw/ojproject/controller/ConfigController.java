package com.yw.ojproject.controller;

import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.dto.WebsiteConfigDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    ReturnData languages()
    {
        //TODO 从judge服务器获取
        return new ReturnData(null, null);
    }
}
