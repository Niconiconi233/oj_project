package com.yw.ojproject.service.impl;

import com.yw.ojproject.bo.CompileSPJBo;
import com.yw.ojproject.bo.ProblemBo;
import com.yw.ojproject.bo.ProblemRuleType;
import com.yw.ojproject.dao.ProblemDao;
import com.yw.ojproject.dao.ProblemTagDao;
import com.yw.ojproject.dto.*;
import com.yw.ojproject.entity.JudgeServer;
import com.yw.ojproject.entity.Problem;
import com.yw.ojproject.entity.ProblemTag;
import com.yw.ojproject.entity.User;
import com.yw.ojproject.service.JudgeServerServer;
import com.yw.ojproject.service.ProblemServer;
import com.yw.ojproject.utils.*;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
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
    JudgeServerServer judgeServerServer;

    @Autowired
    private Environment environment;

    @Autowired
    private ProblemTagDao problemTagDao;

    private ProblemDao problemDao;

    public ProblemServerImpl(ProblemDao problemDao)
    {
        this.dao = problemDao;
        this.problemDao = problemDao;
    }

    @Override
    public ReturnData pickOne()
    {
        List<Problem> l = problemDao.findByVisibleTrue();
        int ran = (int) Math.random()*(l.size());
        return new ReturnData(null, l.get(ran).getId());
    }

    @Override
    public void _add_problem_status(List<Problem> param, HttpServletRequest httpServletRequest)
    {
        /*Cookie cookie = CookieUtils.get(httpServletRequest, "_pid");
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
                if(acm_status.containsKey(tmp.get_id())) {
                    tmp.setMy_status(0);
                }
            }else
            {
                if(io_status.containsKey(tmp.getId()))
                {
                    tmp.setMy_status(0);
                }
            }
        }*/
    }

    @Override
    public void _add_problem_status(Problem param, HttpServletRequest httpServletRequest)
    {
        /*Cookie cookie = CookieUtils.get(httpServletRequest, "_pid");
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
        }*/
    }

    @Override
    public ReturnData findByID(Integer id, HttpServletRequest httpServletRequest)
    {
        Problem p = problemDao.findByidAndVisibleTrue(id);
        if(p == null)
        {
            return new ReturnData("error", "Problem does not exist");
        }
        _add_problem_status(p, httpServletRequest);
        return new ReturnData(null, new ProblemsDto(p));
    }

    @Override
    public ReturnData findById(Integer id)
    {
        Problem p = problemDao.findById(id).orElse(null);
        if(p == null)
        {
            return new ReturnData("error", "Problem does not exist");
        }
        return new ReturnData(null, new AdminProblemDto(p));
    }

    @Override
    public ReturnData uploadTestCase(Boolean spj, MultipartFile file) throws IOException {
        return process_zip(spj, file);
    }

    /**
    * @Description: 解压测试文件 添加info信息
    * @Param: [spj, file]
    * @return: com.yw.ojproject.dto.ReturnData
    * @Author: YW
    * @Date:
    */
    private ReturnData process_zip(Boolean spj, MultipartFile file) throws IOException {
        String fileName = RandUtils.generateRandomFilename();
        File src = null;
        File destDir = null;
        FileOutputStream out = null;
        try{
            src = new File(environment.getProperty("tempPath") + fileName + ".zip");
            file.transferTo(src);
            ZipFile zipFile = new ZipFile(src);
            zipFile.setFileNameCharset("GBK");
            String dest = environment.getProperty("casePath") + fileName;
            destDir = new File(dest);
            zipFile.extractAll(dest);
            List<String> nameList = new LinkedList<>();
            File[] files = destDir.listFiles();
            for(File f :files)
            {
                nameList.add(f.getName());
            }
            List<String> testCaseList = filter_name_list(spj, nameList);
            FileTestCase filecase = new FileTestCase();
            filecase.setSpj(spj);
            List<NormalTestCase> info1 = null;
            List<SpjTestCase> info2 = null;
            if(spj)
            {
                info2 = new LinkedList<>();
                Map<String, SpjTestCase> spjInfo = new HashMap<>();
                byte[] data = new byte[1024];
                for(String str : testCaseList)
                {
                    SpjTestCase tmp = new SpjTestCase();
                    FileInputStream fin = new FileInputStream(dest + "/" +  str);
                    Integer i = fin.read(data);
                    String fdata = new String(data, 0, i);
                    fdata.replace("\r\n", "\n");
                    FileOutputStream fout = new FileOutputStream(dest + "/" + str);
                    fout.write(fdata.getBytes());
                    tmp.setInput_name(str);
                    tmp.setInput_size(fdata.length());
                    Integer idx = str.indexOf(".");
                    String pos = str.substring(0, idx);
                    spjInfo.put(pos, tmp);
                    info2.add(tmp);
                    fin.close();
                    fout.close();
                }
                filecase.setTest_cases(spjInfo);
            }else
            {
                info1 = new LinkedList<>();
                Map<String, NormalTestCase> normalInfo = new HashMap<>();
                byte[] data = new byte[1024];
                for(String str : testCaseList)
                {
                    Integer idx = str.indexOf(".");
                    String pos = str.substring(0, idx);
                    FileInputStream fin = new FileInputStream(dest + "/" + str);
                    Integer i = fin.read(data);
                    String fdata = new String(data, 0, i);
                    fdata.replace("\r\n", "\n");
                    FileOutputStream fout = new FileOutputStream(dest + "/" + str);
                    fout.write(fdata.getBytes());
                    if(str.contains(".out"))
                    {
                        NormalTestCase normalTestCase = normalInfo.get(pos);
                        normalTestCase.setOutput_size(fdata.length());
                        normalTestCase.setOutput_name(str);
                        normalTestCase.setStripped_output_md5(DigestUtils.md5DigestAsHex(fdata.getBytes()));
                        normalInfo.put(pos.toString(), normalTestCase);
                    }else
                    {
                        NormalTestCase normalTestCase = new NormalTestCase();
                        normalTestCase.setInput_name(str);
                        normalTestCase.setInput_size(fdata.length());
                        normalInfo.put(pos, normalTestCase);
                    }
                    fin.close();
                    fout.close();
                }
                for(NormalTestCase value : normalInfo.values())
                {
                    info1.add(value);
                }
                filecase.setTest_cases(normalInfo);
            }
            out = new FileOutputStream(destDir + "/" + "info");
            String info = JsonUtils.objectToJson(filecase);
            out.write(info.getBytes());
            out.close();
            if(info1 != null) {
                return new ReturnData(null, new TestCaseInfoDto(fileName, info1, spj));
            }else if(info2 != null)
            {
                return new ReturnData(null, new TestCaseInfoDto(fileName, info2, spj));
            }else {
                return new ReturnData("error", "system error");
            }
        } catch (IOException | ZipException e) {
            if(src != null) {
                src.delete();
            }
            if(out != null)
            {
                out.close();
            }
            if(destDir != null) {
                destDir.delete();
            }
            return new ReturnData("error", e.getMessage());
        }finally {
            src.delete();
        }
    }

    /**
    * @Description: 获取test_case合格的内容
    * @Param: [spj, name_list]
    * @return: java.util.List<java.lang.String>
    * @Author: YW
    * @Date:
    */
    private List<String> filter_name_list(Boolean spj, List<String> name_list)
    {
        List<String> ret = new LinkedList<>();
        Integer prefix = 1;

        if(spj)
        {
            while(true)
            {
                String in_name = prefix + ".in";
                if(name_list.contains(in_name))
                {
                    ret.add(in_name);
                    ++prefix;
                }else
                {
                    return ret;
                }
            }
        }else
        {
            while(true)
            {
                String in_name = prefix + ".in";
                String out_name = prefix + ".out";
                if(name_list.contains(in_name) && name_list.contains(out_name))
                {
                    ret.add(in_name);
                    ret.add(out_name);
                    ++prefix;
                }else {
                    return ret;
                }
            }
        }
    }

    private String common_checks(ProblemBo problemBo)
    {
        if(problemBo.getSpj())
        {
            if(problemBo.getSpj_language().isEmpty() || problemBo.getSpj_code().isEmpty())
            {
                return "Invaild_spj";
            }
            if(!problemBo.getSpj_compile_ok())
            {
                return "SPJ code must be compiled successfully";
            }
            String data = problemBo.getSpj_language() + problemBo.getSpj_code();
            problemBo.setSpj_version(DigestUtils.md5DigestAsHex(data.getBytes()));
        }else
        {
            problemBo.setSpj_language(null);
            //params.put("spj_language", null);
            problemBo.setSpj_code(null);
            //params.put("spj_code", null);
            if(problemBo.getRule_type().compareTo(ProblemRuleType.IO.getDesc()) == 0)
            {
                problemBo.setTotal_score(100);
            }
        }
        return null;
    }

    @Override
    public ReturnData setProblems(ProblemBo problemBo, HttpServletRequest httpServletRequest)
    {
        //TODO check问题id是否存在
        String error_info = common_checks(problemBo);
        if(error_info != null)
        {
            return new ReturnData("error", error_info);
        }

        Problem problem = new Problem(problemBo);
        List<ProblemTag> tags = new LinkedList<>();
        for(String tmp : problemBo.getTags())
        {
            ProblemTag tag = problemTagDao.findByName(tmp);
            if(tag == null)
            {
                ProblemTag ntg = new ProblemTag();
                ntg.setName(tmp);
                problemTagDao.save(ntg);
                tags.add(ntg);
            }else
            {
                tags.add(tag);
            }
        }
        problem.setTags(tags);
        problem.setLanguages(JsonUtils.listToJsonString(problemBo.getLanguages()));
        String temp = JsonUtils.objectToJson(problem.getTemplates());
        if(temp.compareTo("null") != 0)
        {
            problem.setTemplates(temp);
        }else
        {
            problem.setTemplates(null);
        }
        problem.setIo_mode(JsonUtils.objectToJson(problemBo.getIo_mode()));
        problem.setSamples(JsonUtils.listToJsonString(problemBo.getSamples()));
        Cookie cookie = CookieUtils.get(httpServletRequest, "csrftoken");
        User user = JsonUtils.jsonStringToObject((String)redisUtils.get(cookie.getValue()), User.class);
        problem.setCreate_by(user);
        problemDao.save(problem);
        return new ReturnData(null, problem);
    }


    @Override
    public ReturnData putProblems(AdminProblemDto adminProblemDto)
    {
        Problem p = problemDao.findById(adminProblemDto.getId()).orElse(null);
        if(p == null)
        {
            return new ReturnData("error", "Problem Not Exists");
        }
        p.modProblem(adminProblemDto);
        List<ProblemTag> tags = new LinkedList<>();
        for(String tmp : adminProblemDto.getTags())
        {
            ProblemTag tag = problemTagDao.findByName(tmp);
            if(tag == null)
            {
                ProblemTag ntg = new ProblemTag();
                ntg.setName(tmp);
                problemTagDao.save(ntg);
                tags.add(ntg);
            }else
            {
                tags.add(tag);
            }
        }
        p.setTags(tags);
        problemDao.save(p);
        return new ReturnData();
    }

    @Override
    @Transactional
    public ReturnData delProblems(Integer id)
    {
        problemDao.deleteById(id);
        return new ReturnData();
    }

    @Override
    @Transactional
    public ReturnData compileSPJ(CompileSPJBo compileSPJBo) {
        String spj_version = RandUtils.getRandomString(8);
        JudgeServer judgeServer = judgeServerServer.chooseJudgeServer();
        if(judgeServer == null)
        {
            return new ReturnData("error", "no oj server avaliable");
        }
        System.out.println(spj_version);
        Map<String, Object> params = new HashMap<>();
        params.put("spj_version", spj_version);
        params.put("src", compileSPJBo.getSpj_code());
        if(compileSPJBo.getSpj_language().compareTo("C++") == 0)
        {
            params.put("language_type", "cpp_lang");
        }else
        {
            params.put("language_type", "c_lang");
        }
        //TODO fix url
        ReturnData ans = RequestUtils.sendPostRequest("http://127.0.0.1:10088/compile_spj", params, judgeServer.getToken());
        judgeServerServer.releaseJudgeServer(judgeServer);
        if(((String)ans.getData()).compareTo("success") != 0)
        {
            ans.setError("error");
        }
        return ans;
    }

    @Override
    public List<Problem> findByTag(ProblemTag problemTag)
    {
        return problemDao.findAllByTags(problemTag);
    }
}
