package com.yw.ojproject.judge;

import com.yw.ojproject.bo.JudgeStatus;
import com.yw.ojproject.bo.ProblemIOModeBo;
import com.yw.ojproject.bo.ProblemRuleType;
import com.yw.ojproject.dao.ProblemDao;
import com.yw.ojproject.dao.SubmissionDao;
import com.yw.ojproject.dao.UserProfileDao;
import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.dto.VoProblems;
import com.yw.ojproject.entity.*;
import com.yw.ojproject.service.JudgeServerServer;
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

    @Autowired
    private UserProfileDao userProfileDao;

    //FIXME 优化结构
    public void judge(String id)
    {
        Map<String, Object> params = new HashMap<>();
        Submission submission = submissionDao.findById(id).orElse(null);
        if(submission == null)
        {
            log.warn("submission is null");
            return;
        }
        Problem problem = submission.getProblem();
        problem.add_submission_number();
        JudgeServer judgeServer = judgeServerServer.chooseJudgeServer();
        if(judgeServer == null)
        {
            //没有服务器的情况下，再次放入队列中
            log.warn("judgeServer is null");
            redisUtils.lPushR(environment.getProperty("judge.queue.name"), id);
            //rabbitTemplate.setExchange(environment.getProperty("judge.exchange.name"));
            //rabbitTemplate.setRoutingKey(environment.getProperty("judge.routing.key.name"));
            //rabbitTemplate.convertAndSend(id);
            return;
        }
        params.put("language_type", LanguageUtils.getLanguageType(submission.getLanguage()));
        params.put("src", submission.getCode());
        params.put("max_cpu_time", problem.getTime_limit());
        params.put("max_memory", problem.getMemory_limit() * 1024 * 1024);
        params.put("test_case_id", problem.getTest_case_id());
        //TODO 是否需要输出
        params.put("output", false);
        params.put("spj_version", problem.getSpj_version());
        params.put("spj_src", problem.getSpj_code());
        params.put("io_mode", JsonUtils.jsonStringToObject(problem.getIo_mode(), ProblemIOModeBo.class));
        //更新状态
        submission.setResult(JudgeStatus.JUDGING.getCode());
        submissionDao.save(submission);
        //TODO fix url
        ReturnData ret = RequestUtils.sendPostRequest("http://127.0.0.1:10088/judge", params, judgeServer.getToken());
        judgeServerServer.releaseJudgeServer(judgeServer);
        if(ret == null)
        {
            submission.setResult(JudgeStatus.COMPILE_ERROR.getCode());
            submissionDao.save(submission);
            return;
        }
        //发生错误
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
                if(((int)m.get("result")) != 0)
                {
                    if(problem.getRule_type() == ProblemRuleType.ACM)
                    {
                        submission.setResult((int)m.get("result"));
                        submissionDao.save(submission);
                        return;
                    }else
                    {
                        submission.setResult(JudgeStatus.PARTIALLY_ACCEPTED.getCode());
                        submissionDao.save(submission);
                        return;
                    }
                }
                //AC了
                problem.add_ac_number();
                Map<String, Integer> static_info = JsonUtils.jsonStringToObject(submission.getStatistic_info(), Map.class);
                static_info.put("time_cost", max_cpu);
                static_info.put("memory_cost", max_mem / 1024 / 1024);
                submission.setStatistic_info(JsonUtils.objectToJson(static_info));
            }
            submission.setResult(JudgeStatus.ACCEPTED.getCode());
        }
        User user = submission.getUser();
        UserProfile userProfile = userProfileDao.findByUser(user);
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
        //FIXME 先默认AC
        userProfile.setAcceptnumber(userProfile.getAcceptnumber() + 1);
        userProfileDao.save(userProfile);
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
        submissionDao.save(submission);
        processTask();
    }

    public void processTask()
    {
        String qname = environment.getProperty("judge.queue.name");
        if(redisUtils.lGetListSize(qname) > 0) {
            String id = (String) redisUtils.lPopL(qname);
            judge(id);
        }
    }
}
