package com.yw.ojproject.dao;

import com.yw.ojproject.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProblemDao extends JpaSpecificationExecutor<Problem>, JpaRepository<Problem, Integer> {
    /**
    * @Description: 随机一题
    * @Param: []
    * @return: com.yw.ojproject.entity.Problem
    * @Author: YW
    * @Date: 
    */
    List<Problem> findByVisible(Integer visible);
    
    /**
    * @Description: 通过id查找
    * @Param: [id]
    * @return: com.yw.ojproject.entity.Problem
    * @Author: YW
    * @Date: 
    */
    Problem findByIdAndVisible(Integer id, Integer visible);
}
