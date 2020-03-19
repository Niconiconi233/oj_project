package com.yw.ojproject.service.impl;

import com.yw.ojproject.bo.ProblemRuleType;
import com.yw.ojproject.dao.ProblemDao;
import com.yw.ojproject.dto.ProblemsDto;
import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.dto.VoProblems;
import com.yw.ojproject.entity.Problem;
import com.yw.ojproject.entity.UserProfile;
import com.yw.ojproject.service.ProblemServer;
import com.yw.ojproject.utils.CookieUtils;
import com.yw.ojproject.utils.JsonUtils;
import com.yw.ojproject.utils.RedisUtils;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-14 20:23
**/

@Service
public class ProblemServerImpl extends BaseServerImpl<Problem> implements ProblemServer {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private Environment environment;

    private ProblemDao problemDao;

    public ProblemServerImpl(ProblemDao problemDao)
    {
        this.dao = problemDao;
        this.problemDao = problemDao;
    }

    @Override
    public ReturnData pickOne()
    {
        List<Problem> l = problemDao.findByVisible(1);
        int ran = (int) Math.random()*(l.size());
        return new ReturnData(null, new ProblemsDto(l.get(ran)));
    }

    @Override
    public void _add_problem_status(List<Problem> param, HttpServletRequest httpServletRequest)
    {
        Cookie cookie = CookieUtils.get(httpServletRequest, "_pid");
        //未登录情况下或者过期情况下不进行下一步判断
        if(cookie == null) {
            return;
        }
        //可能存在问题
        String pstr = (String)redisUtils.get(cookie.getValue());
        UserProfile up = JsonUtils.jsonStringToObject(pstr, UserProfile.class);
        Map<String, VoProblems> acm_status = JsonUtils.jsonStringToObject(up.getAcm_problems_status(), Map.class);
        Map<String, VoProblems> io_status = JsonUtils.jsonStringToObject(up.getIo_problems_status(), Map.class);

        for(Problem tmp : param)
        {
            if(tmp.getRule_type() == ProblemRuleType.ACM)
            {
                if(acm_status.containsKey(tmp.getId())) {
                    tmp.setMy_status(0);
                }
            }else
            {
                if(io_status.containsKey(tmp.getId()))
                {
                    tmp.setMy_status(0);
                }
            }
        }
    }

    @Override
    public void _add_problem_status(Problem param, HttpServletRequest httpServletRequest)
    {
        Cookie cookie = CookieUtils.get(httpServletRequest, "_pid");
        //未登录情况下或者过期情况下不进行下一步判断
        if(cookie == null) {
            return;
        }
        //可能存在问题
        String pstr = (String)redisUtils.get(cookie.getValue());
        UserProfile up = JsonUtils.jsonStringToObject(pstr, UserProfile.class);
        Map<String, VoProblems> acm_status = JsonUtils.jsonStringToObject(up.getAcm_problems_status(), Map.class);
        Map<String, VoProblems> io_status = JsonUtils.jsonStringToObject(up.getIo_problems_status(), Map.class);

        if(param.getRule_type() == ProblemRuleType.ACM)
        {
            if(acm_status.containsKey(param.getId())) {
                param.setMy_status(0);
            } else
            {
                if(io_status.containsKey(param.getId())) {
                    param.setMy_status(0);
                }
            }
        }
    }

    @Override
    public ReturnData findByID(Integer id, HttpServletRequest httpServletRequest)
    {
        Problem p = problemDao.findByIdAndVisible(id, 1);
        if(p == null)
        {
            return new ReturnData("error", "Problem does not exist");
        }
        _add_problem_status(p, httpServletRequest);
        return new ReturnData("", new ProblemsDto(p));
    }

    @Override
    public ReturnData findById(Integer id)
    {
        Problem p = problemDao.findById(id).orElse(null);
        if(p == null)
        {
            return new ReturnData("error", "Problem does not exist");
        }
        return new ReturnData(null, p);
    }

    @Override
    public ReturnData uploadTestCase(Boolean spj, MultipartFile file)
    {
        String fileName = file.getOriginalFilename();
        File src = new File(environment.getProperty("tempPath") + fileName);
        try{
            file.transferTo(src);
            ZipFile zipFile = new ZipFile(src);
            zipFile.setFileNameCharset("UTF-8");
            String dest = environment.getProperty("casePath") + fileName;
            File destDir = new File(dest);
            zipFile.extractAll(dest);
            return new ReturnData();
        } catch (IOException | ZipException e) {
            return new ReturnData();
        }
    }
}
