package com.yw.ojproject.controller;

import com.yw.ojproject.aop.SuperadminRequired;
import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.service.AnnouncementServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-13 19:16
**/
@RestController
@RequestMapping("/api")
public class AnnouncementController {

    @Autowired
    AnnouncementServer announcementServer;

    @GetMapping("/announcement")
    ReturnData announcement()
    {
      return announcementServer.getAnnouncement();
    }

    @SuperadminRequired
    @PostMapping("/announcement")
    ReturnData setAnnouncement(@RequestParam String title, @RequestParam String content, @RequestParam Boolean visible, HttpServletRequest httpServletRequest)
    {
        return announcementServer.setAnnouncement(title, content, visible, httpServletRequest);
    }

}


















