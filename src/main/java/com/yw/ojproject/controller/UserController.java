package com.yw.ojproject.controller;

import com.google.code.kaptcha.Producer;
import com.yw.ojproject.aop.LoginRequired;
import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.dto.SessionDto;
import com.yw.ojproject.service.UserServer;
import com.yw.ojproject.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
public class UserController {

    @Autowired
    private UserServer userServer;

    @Autowired
    private Producer captchaProducer;

    @PostMapping("/register")
    ReturnData userRegister(@RequestParam String username, @RequestParam String password, @RequestParam String email)
    {
        return userServer.userRegister(username, password, email);
    }

    @PostMapping("/login")
    ReturnData userLogin(@RequestParam String username, @RequestParam String password, HttpServletResponse httpResponse)
    {
        return userServer.userLogin(username, password, httpResponse);
    }

    @LoginRequired
    @GetMapping("/logout")
    ReturnData userLogout(HttpServletRequest httpServletRequest)
    {
        return userServer.userLogout(httpServletRequest);
    }

    @PostMapping("/check_username_or_email")
    ReturnData checkUsernameOrEmail(@RequestParam(required = false, defaultValue = "") String email, @RequestParam(required = false, defaultValue = "") String username)
    {
        return userServer.checkUsernameOrEmail(email, username);
    }

    @LoginRequired
    @PostMapping("/change_email")
    ReturnData changeEmail(@RequestParam String password, @RequestParam String old_email, @RequestParam String new_email, HttpServletRequest httpServletRequest)
    {
        return userServer.changeEmail(password, old_email, new_email, httpServletRequest);
    }

    @LoginRequired
    @PostMapping("/change_password")
    ReturnData changePassword(@RequestParam String old_password, @RequestParam String new_password, HttpServletRequest httpServletRequest)
    {
        return userServer.changePassword(old_password, new_password, httpServletRequest);
    }

    @LoginRequired
    @GetMapping("/sessions")
    ReturnData session(HttpServletRequest httpServletRequest)
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
        return new ReturnData("", l);
    }

    @GetMapping("/captcha")
    ReturnData captcha() throws IOException {
        String captext = captchaProducer.createText();
        System.out.println(captext);
        BufferedImage bi = captchaProducer.createImage(captext);
        BASE64Encoder encoder = new BASE64Encoder();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", baos);
        byte[] bytes = baos.toByteArray();
        String png_64 = encoder.encode(bytes).trim();
        png_64 = png_64.replace("\n", "").replace("\r", "");
        png_64 = "data:image/png;base64," + png_64;
        return new ReturnData("", png_64);
    }


}
