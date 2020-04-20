package com.yw.ojproject.judge;

import com.yw.ojproject.enums.JudgeStatus;
import com.yw.ojproject.bo.ProblemIOModeBo;
import com.yw.ojproject.enums.ProblemRuleType;
import com.yw.ojproject.dao.ProblemDao;
import com.yw.ojproject.dao.SubmissionDao;
import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.dto.VoProblems;
import com.yw.ojproject.entity.*;
import com.yw.ojproject.service.JudgeServerServer;
import com.yw.ojproject.service.impl.UserProfileServerImpl;
import com.yw.ojproject.utils.JsonUtils;
import com.yw.ojproject.utils.LanguageUtils;
import com.yw.ojproject.utils.RedisUtils;
import com.yw.ojproject.utils.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @program: ojproject
*
* @description: 异步请求评测
*
* @author: YW
*
* @create: 2020-04-02 15:35
**/
@Slf4j
@Component
@Service
@Async("taskExecutor")
public class Dispatcher {
    @Autowired
    private SubmissionDao submissionDao;

    @Autowired
    private JudgeServerServer judgeServerServer;

    @Autowired
    private Environment environment;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ProblemDao problemDao;

    //FIXME 优化结构
    public void judge(String id, String _pid)
    {
        Submission submission = submissionDao.findById(id).orElse(null);
        if(submission == null)
        {
            log.warn("submission is null");
            return;
        }
        //获取评测服务器
        JudgeServer judgeServer = judgeServerServer.chooseJudgeServer();
        if(judgeServer == null)
        {
            //没有服务器的情况下，再次放入队列中
            log.warn("judgeServer is null");
            redisUtils.lPushR(environment.getProperty("judge.queue.name"), id);
            return;
        }

        //此时条件具备
        Problem problem = submission.getProblem();
        problem.add_submission_number();
        //更新评测状态
        submission.setResult(JudgeStatus.JUDGING.getCode());
        submissionDao.save(submission);
        //发送请求
        ReturnData ret = sendRequests(submission, problem, judgeServer);
        judgeServerServer.releaseJudgeServer(judgeServer);
        if(ret == null)
        {
            log.warn("the return data is null");
            submission.setResult(JudgeStatus.SYSTEM_ERROR.getCode());
            submissionDao.save(submission);
            problemDao.save(problem);
            return;
        }

        //发生错误
        //这种情况下一般是编译出错
        if(ret.getError() != null)
        {
            submission.setResult(JudgeStatus.COMPILE_ERROR.getCode());
            Map<String, Object> tmp = JsonUtils.jsonStringToObject(submission.getStatistic_info(), Map.class);
            tmp.put("err_info", ret.getData());
            tmp.put("score", 0);
            submission.setStatistic_info(JsonUtils.objectToJson(tmp));
        }else
        {
            List<Map<String, Object>> data = (List<Map<String, Object>>) ret.getData();
            submission.setInfo(JsonUtils.objectToJson(ret));
            int max_cpu = 0;
            int max_mem = 0;
            for(Map<String, Object> m : data)
            {
                //ACM模式下,多个测试点全部正确则AC，否则取第一个错误的测试点的状态
                //OI模式下, 若多个测试点全部正确则AC， 若全部错误则取第一个错误测试点状态，否则为部分正确
                max_cpu = Math.max((int) m.get("cpu_time"), max_cpu);
                max_mem = Math.max((int)m.get("memory"), max_mem);
                //其中一个测试用例有错
                if(((int)m.get("result")) != 0)
                {
                    if(problem.getRule_type() == ProblemRuleType.ACM)
                    {
                        submission.setResult((int)m.get("result"));
                    }else
                    {
                        submission.setResult(JudgeStatus.PARTIALLY_ACCEPTED.getCode());
                    }
                }
            }
            Map<String, Integer> static_info = JsonUtils.jsonStringToObject(submission.getStatistic_info(), Map.class);
            static_info.put("time_cost", max_cpu);
            static_info.put("memory_cost", max_mem / 1024 / 1024);
            submission.setStatistic_info(JsonUtils.objectToJson(static_info));
            //没有任何错误发生 此时为AC状态
            if(submission.getResult().equals(JudgeStatus.JUDGING.getCode())){
                problem.add_ac_number();
                submission.setResult(JudgeStatus.ACCEPTED.getCode());
            }
        }
        updateUserStatus(submission, problem, _pid);
        updateProblemStatus(problem, submission);
        submissionDao.save(submission);
        processTask();
    }


    private void updateUserStatus(Submission submission, Problem problem, String _pid)
    {
        //FIXME 缓存没有刷新
        User user = submission.getUser();
        UserProfile userProfile = UserProfileServerImpl.getProfileByPid(_pid);
        //更新用户状态
        if(problem.getRule_type() == ProblemRuleType.ACM)
        {
            Map<String, VoProblems> map = JsonUtils.jsonStringToObject(userProfile.getAcm_problems_status(), Map.class);
            map.put(problem.getId().toString(), new VoProblems(problem.getId().toString(), 0, submission.getResult()));
            userProfile.setAcm_problems_status(JsonUtils.objectToJson(map));
        }else
        {
            Map<String, VoProblems> map = JsonUtils.jsonStringToObject(userProfile.getIo_problems_status(), Map.class);
            map.put(problem.getId().toString(), new VoProblems(problem.getId().toString(), 0, submission.getResult()));
            userProfile.setIo_problems_status(JsonUtils.objectToJson(map));
        }
        userProfile.setSubmissionnumber(userProfile.getSubmissionnumber() + 1);
        if(submission.getResult() == 0)
        {
            userProfile.setAcceptnumber(userProfile.getAcceptnumber() + 1);
        }
        userProfile.setHasChanged(true);
        UserProfileServerImpl.putProfileByPid(_pid, userProfile);
        //缓式更新缓存，先删除
        redisUtils.del(_pid);
    }

    private void updateProblemStatus(Problem problem, Submission submission)
    {
        //更新问题状态
        Map<String, Integer> status = JsonUtils.jsonStringToObject(problem.getStatistic_info(), Map.class);
        Integer tmp = status.get(submission.getResult());
        if(tmp == null)
        {
            status.put(submission.getResult().toString(), 1);
        }else
        {
            status.put(submission.getResult().toString(), tmp + 1);
        }
        problem.setStatistic_info(JsonUtils.objectToJson(status));
        problemDao.save(problem);
    }

    private ReturnData sendRequests(Submission submission, Problem problem, JudgeServer judgeServer)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("language_type", LanguageUtils.getLanguageType(submission.getLanguage()));
        params.put("src", submission.getCode());
        params.put("max_cpu_time", problem.getTime_limit());
        params.put("max_memory", problem.getMemory_limit() * 1024 * 1024);
        params.put("test_case_id", problem.getTest_case_id());
        //FIXME 是否需要输出
        params.put("output", false);
        params.put("spj_version", problem.getSpj_version());
        params.put("spj_language", LanguageUtils.getLanguageType(problem.getSpj_language()));
        params.put("spj_src", problem.getSpj_code());
        params.put("io_mode", JsonUtils.jsonStringToObject(problem.getIo_mode(), ProblemIOModeBo.class));
        //FIXME 替换url
        //ReturnData ans = RequestUtils.sendPostRequest(judgeServer.getService_url() + "/judge", params, judgeServer.getToken());
        ReturnData ans = RequestUtils.sendPostRequest( "http://localhost:8081/judge", params, judgeServer.getToken());
        return ans;
    }

    public void processTask()
    {
        String qname = environment.getProperty("judge.queue.name");
        if(redisUtils.lGetListSize(qname) > 0) {
            Map<String, String> data = (Map<String, String>) redisUtils.lPopL(qname);
            judge(data.get("id"), data.get("_pid"));
        }
    }
}
