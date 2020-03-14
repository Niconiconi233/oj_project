package com.yw.ojproject.controller;

import com.yw.ojproject.aop.LoginRequired;
import com.yw.ojproject.aop.SuperadminRequired;
import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.service.AnnouncementServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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
    ReturnData setAnnouncement(@RequestBody Map<String, String> args, HttpServletRequest httpServletRequest)
    {
        Boolean visible = args.get("visible").compareTo("true") == 0 ? true : false;
        return announcementServer.setAnnouncement(args.get("title"), args.get("content"), visible, httpServletRequest);
    }

    @SuperadminRequired
    @DeleteMapping("/announcement")
    ReturnData delAnnouncement(@RequestParam String id)
    {
        return announcementServer.delAnnouncement(id);
    }

}


















