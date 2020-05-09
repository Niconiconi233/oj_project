package com.yw.ojproject.controller;

import com.google.code.kaptcha.Producer;
import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.dto.SessionDto;
import com.yw.ojproject.dto.UserListDto;
import com.yw.ojproject.dto.UserTotalDto;
import com.yw.ojproject.entity.User;
import com.yw.ojproject.service.UserServer;
import com.yw.ojproject.utils.CookieUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
* @program: ojproject
*
* @description: user controller
*
* @author: YW
*
* @create: 2020-03-11 18:00
**/
@RestController
@RequestMapping(value = "/api")
public class UserController extends BaseController<User> {

    private UserServer userServer;

    @Autowired
    private Producer captchaProducer;

    public UserController(UserServer userServer)
    {
        this.baseService = userServer;
        this.userServer = userServer;
    }

    /**
    * @Description: 用户注册
    * @Param: [args]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    @PostMapping("/register")
    public ReturnData userRegister(@RequestBody Map<String, String> args)
    {
        return userServer.userRegister(args.get("username"), args.get("password"), args.get("email"));
    }

    /**
    * @Description: 用户登录
    * @Param: [args, httpResponse]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    @PostMapping("/login")
    public ReturnData userLogin(@RequestBody Map<String, String> params, HttpServletResponse httpServletResponse)
    {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(params.get("username"), params.get("password"));
        try{
            subject.login(token);
        }catch (AccountException e)
        {
            throw e;
        }
        if(subject.isAuthenticated())
        {
            userServer.userLoginInit(params.get("username"), httpServletResponse);
            return new ReturnData(null, subject.getSession().getId());
        }else
        {
            token.clear();
            return new ReturnData("error", "登录失败");
        }
    }

    /**
    * @Description: 登出
    * @Param: [httpServletRequest, httpServletResponse]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    @RequiresPermissions(value = {"user:norm", "user:admin", "user:superadmin"}, logical = Logical.OR)
    @GetMapping("/logout")
    public ReturnData userLogout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
    {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        userServer.userLogout(httpServletRequest, httpServletResponse);
        return new ReturnData(null, "Succeeded");
    }

    /**
    * @Description: 检查邮件，用户名
    * @Param: [args]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    @PostMapping("/check_username_or_email")
    public ReturnData checkUsernameOrEmail(@RequestBody Map<String, String> args)
    {
        return userServer.checkUsernameOrEmail(args.get("email"), args.get("username"));
    }

    /**
    * @Description: 更改邮箱
    * @Param: [args, httpServletRequest]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    @RequiresPermissions(value = {"user:norm", "user:admin", "user:superadmin"}, logical = Logical.OR)
    @PostMapping("/change_email")
    public ReturnData changeEmail(@RequestBody Map<String, String> args, HttpServletRequest httpServletRequest)
    {
        return userServer.changeEmail(args.get("password"), args.get("old_email"), args.get("new_email"), httpServletRequest);
    }

    /**
    * @Description: 修改密码
    * @Param: [args, httpServletRequest]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    @RequiresPermissions(value = {"user:norm", "user:admin", "user:superadmin"}, logical = Logical.OR)
    @PostMapping("/change_password")
    public ReturnData changePassword(@RequestBody Map<String, String> args, HttpServletRequest httpServletRequest)
    {
        return userServer.changePassword(args.get("old_password"), args.get("new_password"), httpServletRequest);
    }

    /**
    * @Description: 获取session
    * @Param: [httpServletRequest]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    @RequiresPermissions(value = {"user:norm", "user:admin", "user:superadmin"}, logical = Logical.OR)
    @GetMapping("/sessions")
    public ReturnData session(HttpServletRequest httpServletRequest)
    {
        String ua = httpServletRequest.getHeader("User_Agent");
        SessionDto s = new SessionDto();
        s.setIp("");
        s.setLast_activity(new Date());
        Cookie cookie = CookieUtils.get(httpServletRequest, "csrftoken");
        s.setSession_key(cookie.getValue());
        s.setUser_agent(ua);
        List<SessionDto> l = new LinkedList<>();
        l.add(s);
        return new ReturnData(null, l);
    }

    /**
    * @Description: 获取二维码
    * @Param: []
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    @GetMapping("/captcha")
    public ReturnData captcha() throws IOException {
        String captext = captchaProducer.createText();
        BufferedImage bi = captchaProducer.createImage(captext);
        BASE64Encoder encoder = new BASE64Encoder();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", baos);
        byte[] bytes = baos.toByteArray();
        String png_64 = encoder.encode(bytes).trim();
        png_64 = png_64.replace("\n", "").replace("\r", "");
        png_64 = "data:image/png;base64," + png_64;
        return new ReturnData(null, png_64);
    }

    /**
    * @Description: 管理员接口 查询用户
    * @Param: [params]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date: 
    */
    @RequiresRoles(value = {"admin", "superadmin"}, logical = Logical.OR)
    @GetMapping("/admin/user")
    public ReturnData adminGetUser(@RequestParam Map<String, String> params)
    {
        if(params.containsKey("id"))
        {
            User u = userServer.findById(Integer.valueOf(params.get("id")));
            if(u == null)
            {
                return new ReturnData("error", "User does not exist");
            }
            return new ReturnData(null, new UserTotalDto(u));
        }
        Map<String, String> args = new HashMap<>();
        args.put("size", params.get("limit"));
        Integer offset = Integer.valueOf(params.get("offset"));
        if(offset < 0)
        {
            offset = 0;
        }
        Integer page =  offset / Integer.valueOf(params.get("limit")) + 1;
        args.put("page", page.toString());
        if(params.containsKey("keyword")) {
            args.put("username_lk", "%" + params.get("keyword") + "%");
        }
        args.put("sort", "ctime,ASC");
        Page<User> result = findAllPageByParams(args);
        UserListDto ans = new UserListDto(result);
        return new ReturnData(null, ans);
    }


    /**
    * @Description: 管理员接口 修改用户
    * @Param: [userTotalDto]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date: 
    */
    @RequiresRoles(value = {"admin", "superadmin"}, logical = Logical.OR)
    @PutMapping("/admin/user")
    public ReturnData adminPutUser(@RequestBody UserTotalDto userTotalDto)
    {
        return userServer.modUser(userTotalDto);
    }

    /**
    * @Description: 管理员接口 删除用户
    * @Param: [id]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date: 
    */
    @RequiresRoles("superadmin")
    @DeleteMapping("/admin/user")
    public ReturnData adminDelUser(@RequestParam Integer id)
    {
        return userServer.delUser(id);
    }


}
