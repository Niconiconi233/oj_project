package com.yw.ojproject.service.impl;

import com.yw.ojproject.bo.JudgeStatus;
import com.yw.ojproject.bo.ProblemIOModeBo;
import com.yw.ojproject.bo.ProblemRuleType;
import com.yw.ojproject.dao.SubmissionDao;
import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.entity.JudgeServer;
import com.yw.ojproject.entity.Problem;
import com.yw.ojproject.entity.Submission;
import com.yw.ojproject.service.DispatcherServer;
import com.yw.ojproject.service.JudgeServerServer;
import com.yw.ojproject.utils.JsonUtils;
import com.yw.ojproject.utils.LanguageUtils;
import com.yw.ojproject.utils.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
* @description: 
*
* @author: YW
*
* @create: 2020-04-03 20:39
**/

@Slf4j
@Component
@Service
@Async("taskExecutor")
public class DispatcherServerImpl implements DispatcherServer {
    @Autowired
    private SubmissionDao submissionDao;

    @Autowired
    private Environment environment;

    @Autowired
    private JudgeServerServer judgeServerServer;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
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
        JudgeServer judgeServer = judgeServerServer.chooseJudgeServer();
        if(judgeServer == null)
        {
            //没有服务器的情况下，再次放入队列中
            rabbitTemplate.setExchange(environment.getProperty("judge.exchange.name"));
            rabbitTemplate.setRoutingKey(environment.getProperty("judge.routing.key.name"));
            rabbitTemplate.convertAndSend(id);
            return;
        }
        params.put("language_type", LanguageUtils.getLanguageType(submission.getLanguage()));
        params.put("src", submission.getCode());
        params.put("max_cpu_time", problem.getTime_limit());
        params.put("max_memory", problem.getMemory_limit());
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
        List<Map<String, Object>> data = (List<Map<String, Object>>) ret.getData();
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
            submission.setInfo(JsonUtils.objectToJson(ret));
            //TODO 更新static_info
            for(Map<String, Object> m : data)
            {
                //ACM模式下,多个测试点全部正确则AC，否则取第一个错误的测试点的状态
                //OI模式下, 若多个测试点全部正确则AC， 若全部错误则取第一个错误测试点状态，否则为部分正确
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
            }
            submission.setResult(JudgeStatus.ACCEPTED.getCode());
        }
        submissionDao.save(submission);
    }
}
