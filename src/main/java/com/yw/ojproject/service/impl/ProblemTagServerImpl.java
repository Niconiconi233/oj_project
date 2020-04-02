package com.yw.ojproject.service.impl;

import com.yw.ojproject.dao.ProblemTagDao;
import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.entity.ProblemTag;
import com.yw.ojproject.service.ProblemTagServer;
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
* @create: 2020-03-14 20:29
**/

@Service
public class ProblemTagServerImpl implements ProblemTagServer {

    @Autowired
    ProblemTagDao problemTagDao;

    @Override
    public ReturnData getProblemTag()
    {
        List<ProblemTag> list = problemTagDao.findAll();
        return new ReturnData(null, list);
    }

    @Override
    public ProblemTag findTagByName(String name)
    {
        return problemTagDao.findByName(name);
    }
}
