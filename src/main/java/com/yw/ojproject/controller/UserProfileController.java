package com.yw.ojproject.controller;

import com.yw.ojproject.aop.LoginRequired;
import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.entity.UserProfile;
import com.yw.ojproject.service.UserProfileServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
* @program: ojproject
*
* @description: 用户配置 控制器
*
* @author: YW
*
* @create: 2020-03-13 13:50
**/
@RestController
@RequestMapping("/api")
public class UserProfileController extends BaseController<UserProfile> {

    private UserProfileServer userProfileService;

    public UserProfileController(UserProfileServer userProfileService)
    {
        this.baseService = userProfileService;
        this.userProfileService = userProfileService;
    }

    @GetMapping("/user_rank")
    public ReturnData getUserRank(@RequestParam Map<String, String> pargrams)
    {
        String type = pargrams.get("rule");
        Map<String, String> args = new HashMap<>();
        args.put("page", pargrams.get("offset"));
        args.put("size", pargrams.get("limit"));
        if(type.compareTo("ACM") == 0)
        {
            args.put("submissionnumber_ge", "0");
            args.put("sort", "acceptnumber,DESC");
        }else
        {
            args.put("totalscore_ge", "0");
            args.put("sort", "totalscore,DESC");
        }
        return new ReturnData("", findAllPageByParams(args));
    }

    @LoginRequired
    @GetMapping("/profile")
    ReturnData userProfile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
    {
        return userProfileService.userProfile(httpServletRequest, httpServletResponse);
    }


}





















