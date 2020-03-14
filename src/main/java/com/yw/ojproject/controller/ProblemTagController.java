package com.yw.ojproject.controller;

import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.service.ProblemTagServer;
import org.springframework.beans.factory.annotation.Autowired;
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
* @create: 2020-03-14 20:34
**/

@RestController
@RequestMapping("/api")
public class ProblemTagController {
    @Autowired
    ProblemTagServer problemTagServer;

    @GetMapping("/problem/tags")
    ReturnData getTags()
    {
        return problemTagServer.getProblemTag();
    }
}
