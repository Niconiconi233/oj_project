package com.yw.ojproject.controller;

import com.yw.ojproject.aop.SuperadminRequired;
import com.yw.ojproject.bo.AnnouncementBo;
import com.yw.ojproject.dto.AnnouncementListDto;
import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.entity.Announcement;
import com.yw.ojproject.service.AnnouncementServer;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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
public class AnnouncementController extends BaseController<Announcement> {

    //@Autowired
    AnnouncementServer announcementServer;

    public AnnouncementController(AnnouncementServer announcementServer)
    {
        this.baseService = announcementServer;
        this.announcementServer = announcementServer;
    }

    /**
    * @Description: 公告接口
    * @Param: []
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date: 
    */
    @GetMapping("/announcement")
    public ReturnData announcement()
    {
      return announcementServer.getAnnouncement();
    }

    /**
    * @Description: 管理员接口 设置公告
    * @Param: [args, httpServletRequest]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date: 
    */
    @SuperadminRequired
    @PostMapping("/admin/announcement")
    public ReturnData adminSetAnnouncement(@RequestBody AnnouncementBo announcementBo, HttpServletRequest httpServletRequest)
    {
        return announcementServer.setAnnouncement(announcementBo, httpServletRequest);
    }

    /**
    * @Description: 管理员接口 删除公告
    * @Param: [id]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date: 
    */
    @SuperadminRequired
    @DeleteMapping("/admin/announcement")
    public ReturnData adminDelAnnouncement(@RequestParam Long id)
    {
        return announcementServer.delAnnouncement(id);
    }

    /**
    * @Description: 管理员接口 获取公告
    * @Param: [params]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    @SuperadminRequired
    @GetMapping("/admin/announcement")
    public ReturnData adminGetAnnouncement(@RequestParam Map<String, String> params)
    {
        Map<String, String> args = new HashMap<>();
        args.put("size", params.get("limit"));
        Integer offset = Integer.valueOf(params.get("offset"));
        if(offset < 0)
        {
            offset = 0;
        }
        Integer page =  offset / Integer.valueOf(params.get("limit")) + 1;
        args.put("page", page.toString());
        Page<Announcement> result = findAllPageByParams(args);
        return new ReturnData(null, new AnnouncementListDto(result));
    }

    @SuperadminRequired
    @PutMapping("/admin/announcement")
    public ReturnData adminPutAnnouncement(@RequestBody AnnouncementBo announcementBo)
    {
        return announcementServer.putAnnouncement(announcementBo);
    }



}


















