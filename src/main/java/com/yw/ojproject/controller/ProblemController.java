package com.yw.ojproject.controller;

import com.yw.ojproject.aop.AdminRequired;
import com.yw.ojproject.aop.SuperadminRequired;
import com.yw.ojproject.bo.CompileSPJBo;
import com.yw.ojproject.bo.ProblemBo;
import com.yw.ojproject.dto.AdminProblemDto;
import com.yw.ojproject.dto.ProblemsListDto;
import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.entity.Problem;
import com.yw.ojproject.entity.ProblemTag;
import com.yw.ojproject.service.ProblemServer;
import com.yw.ojproject.service.ProblemTagServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
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

    @Autowired
    private ProblemTagServer problemTagServer;

    private ProblemServer problemServer;

    public ProblemController(ProblemServer problemServer)
    {
        this.baseService = problemServer;
        this.problemServer = problemServer;
    }

    /**
    * @Description: 随机一道题
    * @Param: []
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    @GetMapping("/pickone")
    public ReturnData pickOne()
    {
        return problemServer.pickOne();
    }

    /**
    * @Description: 获取problem 列表
    * @Param: [params, httpServletRequest]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    @GetMapping("/problem")
    public Object problems(@RequestParam Map<String, String> params, HttpServletRequest httpServletRequest)
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
            String tag = params.get("tag");
            ProblemTag problemTag = problemTagServer.findTagByName(tag);
            List<Problem> res = problemServer.findByTag(problemTag);
            problemServer._add_problem_status(res, httpServletRequest);
            return new ReturnData(null, new ProblemsListDto(res, Integer.valueOf(params.get("limit")), Integer.valueOf(params.get("offset"))));
        }

        //通过keyword获取
        if(params.containsKey("keyword"))
        {
            String keyword = params.get("keyword");
            args.put("title_lk", "%" + keyword + "%");
            Page<Problem> result = findAllPageByParams(args);
            problemServer._add_problem_status(result.getContent(), httpServletRequest);
            return new ReturnData(null, new ProblemsListDto(result));
        }
        Page<Problem> result = findAllPageByParams(args);
        problemServer._add_problem_status(result.getContent(), httpServletRequest);
        return new ReturnData(null, new ProblemsListDto(result));
    }

    /**
    * @Description: 管理员接口 获取problem
    * @Param: [params]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    @SuperadminRequired
    @GetMapping("/admin/problem")
    public ReturnData adminGetProblems(@RequestParam Map<String, String> params)
    {
        if(params.containsKey("id"))
        {
            return problemServer.findById(Integer.valueOf(params.get("id")));
        }
        Map<String, String> args = new LinkedHashMap<>();
        args.put("size", params.get("limit"));
        args.put("sort","time,DESC");
        //通过keyword获取
        if(params.containsKey("keyword"))
        {
            String keyword = params.get("keyword");
            args.put("title_lk", "%" + keyword + "%");
            Page<Problem> result = findAllPageByParams(args);
            return new ReturnData(null, new ProblemsListDto(result));
        }
        Page<Problem> result = findAllPageByParams(args);
        return new ReturnData(null, new ProblemsListDto(result));
    }

    /**
    * @Description: 管理员接口 修改问题
    * @Param: [problem]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    @SuperadminRequired
    @PutMapping("/admin/problem")
    public ReturnData adminPutProblems(@RequestBody AdminProblemDto problem)
    {
        return problemServer.putProblems(problem);
    }

    /**
    * @Description: 管理员接口 上传测试文件
    * @Param: [spj, file]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    @SuperadminRequired
    @PostMapping("/admin/test_case")
    public ReturnData uploadTestCase(@RequestParam Boolean spj, @RequestParam("file") MultipartFile file) throws IOException {
        if(file.isEmpty())
        {
            return new ReturnData("error", "Upload failed");
        }
        return problemServer.uploadTestCase(spj, file);
    }

    /**
    * @Description: 管理员接口 添加问题
    * @Param: [b, httpServletRequest]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    @SuperadminRequired
    @PostMapping("/admin/problem")
    public ReturnData setProblems(@RequestBody ProblemBo b, HttpServletRequest httpServletRequest)
    {
        return problemServer.setProblems(b, httpServletRequest);
    }


    /**
    * @Description: 管理员接口 删除问题
    * @Param: [id]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date: 
    */
    @SuperadminRequired
    @DeleteMapping("/admin/problem")
    public ReturnData delProblems(@RequestParam Integer id)
    {
        return problemServer.delProblems(id);
    }

    /**
    * @Description: 管理员接口 编译spj
    * @Param: [compileSPJBo]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date: 
    */
    @AdminRequired
    @PostMapping("/admin/compile_spj")
    public ReturnData adminCompileSPJ(@RequestBody CompileSPJBo compileSPJBo)
    {
        return problemServer.compileSPJ(compileSPJBo);
    }



}
