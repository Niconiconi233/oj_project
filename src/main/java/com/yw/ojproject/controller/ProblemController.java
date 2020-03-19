package com.yw.ojproject.controller;

import com.yw.ojproject.aop.SuperadminRequired;
import com.yw.ojproject.dto.ProblemsDto;
import com.yw.ojproject.dto.ProblemsListDto;
import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.dto.TestCaseInfoDto;
import com.yw.ojproject.entity.Problem;
import com.yw.ojproject.service.ProblemServer;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-14 20:38
**/
@RestController
@RequestMapping("/api")
public class ProblemController extends BaseController<Problem> {

    private ProblemServer problemServer;

    public ProblemController(ProblemServer problemServer)
    {
        this.baseService = problemServer;
        this.problemServer = problemServer;
    }

    @GetMapping("/pickone")
    public ReturnData pickOne()
    {
        return problemServer.pickOne();
    }

    @GetMapping("/problem")
    public ReturnData problems(@RequestParam Map<String, String> params, HttpServletRequest httpServletRequest)
    {
        if(params.containsKey("problem_id"))
        {
            return problemServer.findByID(Integer.valueOf(params.get("problem_id")), httpServletRequest);
        }
        if(!params.containsKey("limit"))
        {
            return new ReturnData("error", "Problem does not exist");
        }
        Map<String, String> args = new LinkedHashMap<>();
        args.put("page", params.get("page"));
        args.put("size", params.get("limit"));
        args.put("visible_eq", "1");
        //按难度获取
        if(params.containsKey("difficulty"))
        {
            String diff = params.get("difficulty");
            if(diff.compareTo("Low") == 0)
            {
                args.put("difficulty_eq", "2");
            }else if(diff.compareTo("Mid") == 0)
            {
                args.put("difficulty_eq", "1");
            }else {
                args.put("difficulty_eq", "0");
            }
            Page<Problem> result = findAllPageByParams(args);
            problemServer._add_problem_status(result.getContent(), httpServletRequest);
            return new ReturnData(null, new ProblemsListDto(result));
        }
        //通过tag获取
        if(params.containsKey("tag"))
        {
            String tag = params.get("difficulty");
            args.put("tags_in", tag);
            Page<Problem> result = findAllPageByParams(args);
            problemServer._add_problem_status(result.getContent(), httpServletRequest);
            return new ReturnData(null, new ProblemsListDto(result));
        }

        //通过keyword获取
        if(params.containsKey("keyword"))
        {
            String keyword = params.get("keyword");
            args.put("title_eq", keyword);
            args.put("id_eq", keyword);
            Page<Problem> result = findAllPageByParams(args);
            problemServer._add_problem_status(result.getContent(), httpServletRequest);
            return new ReturnData(null, new ProblemsListDto(result));
        }
        Page<Problem> result = findAllPageByParams(args);
        problemServer._add_problem_status(result.getContent(), httpServletRequest);
        return new ReturnData(null, new ProblemsListDto(result));
    }

    @SuperadminRequired
    @GetMapping("/admin/problem")
    public ReturnData adminGetProblems(Map<String, String> params)
    {
        if(params.containsKey("id"))
        {
            return problemServer.findById(Integer.valueOf(params.get("id")));
        }
        Map<String, String> args = new LinkedHashMap<>();
        //args.put("page", params.get("page"));
        args.put("size", params.get("limit"));
        args.put("sort","time,DESC");
        //通过keyword获取
        if(params.containsKey("keyword"))
        {
            String keyword = params.get("keyword");
            args.put("title_eq", keyword);
            args.put("id_eq", keyword);
            Page<Problem> result = findAllPageByParams(args);
            return new ReturnData(null, new ProblemsListDto(result));
        }
        Page<Problem> result = findAllPageByParams(args);
        return new ReturnData(null, new ProblemsListDto(result));
    }

    @SuperadminRequired
    @PutMapping("/admin/problem")
    ReturnData adminPutProblems(@RequestBody Problem problem)
    {
        return new ReturnData();
    }

    @SuperadminRequired
    @PostMapping("/admin/test_case")
    ReturnData uploadTestCase(@RequestParam Boolean spj, @RequestParam("file") MultipartFile file)
    {
        if(file.isEmpty())
        {
            return new ReturnData("error", "Upload failed");
        }
        return problemServer.uploadTestCase(spj, file);
    }



}
