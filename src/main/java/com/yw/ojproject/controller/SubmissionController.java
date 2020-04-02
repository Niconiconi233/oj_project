package com.yw.ojproject.controller;
import	java.util.LinkedHashMap;

import com.yw.ojproject.aop.LoginRequired;
import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.dto.SubmissionListDto;
import com.yw.ojproject.entity.Submission;
import com.yw.ojproject.service.SubmissionServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
* @create: 2020-04-02 16:02
**/
@RestController
@RequestMapping("/api")
public class SubmissionController extends BaseController<Submission> {

    //@Autowired
    SubmissionServer submissionServer;

    public SubmissionController(SubmissionServer submissionServer)
    {
        this.baseService = submissionServer;
        this.submissionServer = submissionServer;
    }

    @LoginRequired
    @PostMapping("/submission")
    public ReturnData submission(@RequestBody Map<String, Object> param, HttpServletRequest httpServletRequest)
    {
        return submissionServer.postSubmission(param, httpServletRequest);
    }

    @LoginRequired
    @GetMapping("/submission")
    public ReturnData getSubmission(@RequestParam String id, HttpServletRequest httpServletRequest)
    {
        if(id == null || id.isEmpty())
        {
            return new ReturnData("error", "Parameter id doesn't exist");
        }
        return submissionServer.getSubmission(id, httpServletRequest);
    }

    @LoginRequired
    @PutMapping("/submission")
    public ReturnData putSubmission(@RequestBody Map<String, Object> param)
    {
        String id = (String)param.get("id");
        if(id == null || id.isEmpty())
        {
            return new ReturnData("error", "Parameter id doesn't exist");
        }
        Boolean can_shared = (Boolean) param.get("shared");
        if(can_shared == null)
        {
            return new ReturnData("error", "Shared doesn't exist");
        }
        return submissionServer.putSubmission(id, can_shared);
    }

    @LoginRequired
    @GetMapping("/submissions")
    public ReturnData submissionList(@RequestParam Map<String, String> param, HttpServletRequest httpServletRequest)
    {
        if(!param.containsKey("limit"))
        {
            return new ReturnData("error", "Limit is needed");
        }
        if(param.containsKey("contest_id"))
        {
            return new ReturnData("error", "Parameter error");
        }
        Integer size = Integer.valueOf(param.get("limit"));
        Integer page = Integer.valueOf(param.get("page"));
        Integer offset = Integer.valueOf(param.get("offset"));
        if(param.containsKey("problem_id"))
        {
            //查询问题下的提交目录
            return submissionServer.getSubmissionByProblemId(Integer.valueOf(param.get("problem_id")), offset, size);
        }
        if(param.containsKey("myself") && Integer.valueOf(param.get("myself")).equals(1))
        {
            //查询用户提交记录
            //数据量较小，不使用分页查询
            return submissionServer.getSubmissionByUser(offset, size, httpServletRequest);
        }
        Map<String, String> args = new LinkedHashMap<> ();
        args.put("page", page.toString());
        args.put("size", size.toString());
        args.put("sort", "ctime,DESC");
        if(param.containsKey("username") && !param.get("username").isEmpty())
        {
            //通过名字查询
            //数据量可能较大，使用分页查询
            args.put("username_lk", "%" + (String)param.get("username") + "%");
            Page<Submission> tmp = findAllPageByParams(args);
            return new ReturnData(null, new SubmissionListDto(tmp));
        }
        if(param.containsKey("result") && !param.get("result").isEmpty())
        {
            //通过结果查询
            args.put("result_eq", Integer.valueOf(param.get("result")).toString());
            Page<Submission> tmp = findAllPageByParams(args);
            return new ReturnData(null, new SubmissionListDto(tmp));
        }
        Page<Submission> tmp = findAllPageByParams(args);
        return new ReturnData(null, new SubmissionListDto(tmp));
    }
}
