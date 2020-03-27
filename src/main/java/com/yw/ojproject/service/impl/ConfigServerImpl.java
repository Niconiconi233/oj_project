package com.yw.ojproject.service.impl;

import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.dto.TestCaseDto;
import com.yw.ojproject.service.ConfigServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-27 22:21
**/
@Service
public class ConfigServerImpl implements ConfigServer {

    @Autowired
    private Environment environment;

    @Override
    public ReturnData getAllTestCase()
    {
        File file = new File(environment.getProperty("casePath"));
        List<TestCaseDto> ans = new LinkedList<>();
        File[] files = file.listFiles();
        for(File f : files)
        {
            ans.add(new TestCaseDto(f.getName(), new Date(f.lastModified())));
        }
        return new ReturnData(null, ans);
    }

    @Override
    public ReturnData delTestCase(String id)
    {
        File file = new File(environment.getProperty("casePath") + "/" + id);
        if(file.exists())
        {
            File[] files = file.listFiles();
            for(File f :files)
            {
                f.delete();
            }
            file.delete();
        }
        return new ReturnData();
    }
}
