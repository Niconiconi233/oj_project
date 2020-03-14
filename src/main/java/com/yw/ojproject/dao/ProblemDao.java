package com.yw.ojproject.dao;

import com.yw.ojproject.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProblemDao extends JpaSpecificationExecutor<Problem>, JpaRepository<Problem, String> {
    /**
    * @Description: 随机一题
    * @Param: []
    * @return: com.yw.ojproject.entity.Problem
    * @Author: YW
    * @Date: 
    */
    List<Problem> findByVisibleTrue();
}
