package com.yw.ojproject.service.impl;

import com.yw.ojproject.bo.JudgeStatus;
import com.yw.ojproject.dao.ProblemDao;
import com.yw.ojproject.dao.SubmissionDao;
import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.dto.SubmissionDto;
import com.yw.ojproject.dto.SubmissionListDto;
import com.yw.ojproject.entity.Problem;
import com.yw.ojproject.entity.Submission;
import com.yw.ojproject.entity.User;
import com.yw.ojproject.judge.Dispatcher;
import com.yw.ojproject.service.SubmissionServer;
import com.yw.ojproject.utils.CookieUtils;
import com.yw.ojproject.utils.JsonUtils;
import com.yw.ojproject.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
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
* @create: 2020-04-02 16:02
**/
@Service
public class SubmissionServerImpl extends BaseServerImpl<Submission> implements SubmissionServer {

    @Autowired
    private RedisUtils redisUtils;

    //@Autowired
    //private RabbitTemplate rabbitTemplate;

    @Autowired
    private Environment environment;

    @Autowired
    private ProblemDao problemDao;

    @Autowired
    Dispatcher dispatcher;

    private SubmissionDao submissionDao;

    public SubmissionServerImpl(SubmissionDao submissionDao)
    {
        this.dao = submissionDao;
        this.submissionDao = submissionDao;
    }

    @Override
    public ReturnData postSubmission(Map<String, Object> params, HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
        Problem problem = problemDao.findById((Integer) params.get("problem_id")).orElse(null);
        if (problem == null) {
            return new ReturnData("error", "no such problem");
        }
        List<String> languages = JsonUtils.jsonStringToList(problem.getLanguages(), String.class);
        if (!languages.contains((String) params.get("language"))) {
            return new ReturnData("error", "{data['language']} is now allowed in the problem");
        }
        Cookie cookie = CookieUtils.get(httpServletRequest, "csrftoken");
        User user = JsonUtils.jsonStringToObject((String) redisUtils.get(cookie.getValue()), User.class);
        Submission submission = new Submission(user, problem, (String) params.get("language"), (String) params.get("code"), "");
        //发送到消息队列中
        submissionDao.save(submission);
        redisUtils.lPushR(environment.getProperty("judge.queue.name"), submission.getId());
        dispatcher.processTask();

        //弃用rabbitmq
        //rabbitTemplate.setExchange(environment.getProperty("judge.exchange.name"));
        //rabbitTemplate.setRoutingKey(environment.getProperty("judge.routing.key.name"));
        //rabbitTemplate.convertAndSend(submission.getId());

        Map<String, String> ans = new LinkedHashMap<>();
        ans.put("submission_id", submission.getId());
        return new ReturnData(null, ans);
    }

    @Override
    public ReturnData getSubmission(String id, HttpServletRequest httpServletRequest) {
        Submission submission = submissionDao.findById(id).orElse(null);
        if (submission == null) {
            return new ReturnData("error", "Submission doesn't exist");
        }
        Cookie cookie = CookieUtils.get(httpServletRequest, "csrftoken");
        User user = JsonUtils.jsonStringToObject((String) redisUtils.get(cookie.getValue()), User.class);
        if (!submission.check_user_permission(user)) {
            return new ReturnData("error", "No permission for this submission");
        }
        SubmissionDto submissionDto = new SubmissionDto(submission);
        if (submission.check_user_permission(user)) {
            submissionDto.setCan_unshare(true);
        }
        return new ReturnData(null, new SubmissionDto(submission));
    }

    @Override
    public ReturnData putSubmission(String id, Boolean can_shared) {
        Submission submission = submissionDao.findById(id).orElse(null);
        if (submission == null) {
            return new ReturnData("error", "Submission doesn't exist");
        }
        submission.setShared(can_shared);
        submissionDao.save(submission);
        return new ReturnData();
    }

    @Override
    public ReturnData getSubmissionByProblemId(Integer id, Integer offset, Integer size)
    {
        Problem p = problemDao.findById(id).orElse(null);
        if(p == null)
        {
            return new ReturnData("error", "Problem doesn't exist");
        }
        List<Submission> list = submissionDao.findAllByProblemOrderByCtimeDesc(p);
        //FIXME 只能处理数据量较小的情况
        return new ReturnData(null, new SubmissionListDto(list, offset, size));
    }

    @Override
    public ReturnData getSubmissionByUser(Integer offset, Integer size, HttpServletRequest httpServletRequest)
    {
        Cookie cookie = CookieUtils.get(httpServletRequest, "csrftoken");
        User user = JsonUtils.jsonStringToObject((String) redisUtils.get(cookie.getValue()), User.class);
        if(user == null)
        {
            return new ReturnData(null, "User not exists");
        }
        List<Submission> list = submissionDao.findAllByUserOrderByCtimeDesc(user);
        return new ReturnData(null, new SubmissionListDto(list, offset, size));
    }

    @Override
    public ReturnData submissionExists(Integer porblemId, HttpServletRequest httpServletRequest)
    {
        Problem p = problemDao.findById(porblemId).orElse(null);
        Cookie cookie = CookieUtils.get(httpServletRequest, "csrftoken");
        User user = JsonUtils.jsonStringToObject((String) redisUtils.get(cookie.getValue()), User.class);
        Integer cnt = submissionDao.countByProblemAndUserAndResultEquals(p, user, JudgeStatus.ACCEPTED.getCode());
        if(cnt == 0)
        {
            return new ReturnData(null, false);
        }
        return new ReturnData(null, true);
    }

    @Override
    public ReturnData adminSubmissionRejudge(String id)
    {
        redisUtils.lPushR(environment.getProperty("judge.queue.name"), id);
        dispatcher.processTask();
        return new ReturnData();
    }

}
