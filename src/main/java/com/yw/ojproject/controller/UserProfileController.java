package com.yw.ojproject.controller;

import com.yw.ojproject.bo.UserProfileBo;
import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.dto.UserRankListDto;
import com.yw.ojproject.entity.UserProfile;
import com.yw.ojproject.service.UserProfileServer;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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

    /**
    * @Description: 获取排行信息
    * @Param: [pargrams]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    @GetMapping("/user_rank")
    public ReturnData getUserRank(@RequestParam Map<String, String> pargrams)
    {
        String type = pargrams.get("rule");
        Map<String, String> args = new HashMap<>();
        String page = pargrams.get("offset").compareTo("0") == 0 ? "1" : pargrams.get("offset");
        args.put("page", page);
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
        Page<UserProfile> p = findAllPageByParams(args);
        return new ReturnData(null, new UserRankListDto(p));
    }

    /**
    * @Description: 用户配置文件
    * @Param: [httpServletRequest, httpServletResponse]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    @RequiresPermissions("user:norm")
    @GetMapping("/profile")
    public ReturnData userProfile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws InterruptedException {
        return userProfileService.userProfile(httpServletRequest, httpServletResponse);
    }

    /**
    * @Description: 修改用户配置
    * @Param: [httpServletRequest, httpServletResponse, userProfileBo]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    @RequiresPermissions("user:norm")
    @PutMapping("/profile")
    public ReturnData modUserProfile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestBody UserProfileBo userProfileBo) throws InterruptedException {
        return userProfileService.modUserProfile(httpServletRequest, httpServletResponse, userProfileBo);
    }
}





















