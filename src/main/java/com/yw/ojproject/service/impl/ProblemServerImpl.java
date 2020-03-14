package com.yw.ojproject.service.impl;

import com.yw.ojproject.dao.ProblemDao;
import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.entity.Problem;
import com.yw.ojproject.service.ProblemServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
public class ProblemServerImpl implements ProblemServer {

    @Autowired
    ProblemDao problemDao;

    @Override
    public ReturnData pickOne()
    {
        List<Problem> l = problemDao.findByVisibleTrue();
        int ran = (int) Math.random()*(l.size());
        return new ReturnData(null, l.get(ran).getId());
    }
}
