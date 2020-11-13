package com.yw.ojproject.dao;

import com.yw.ojproject.entity.Problem;
import com.yw.ojproject.entity.ProblemTag;
import com.yw.ojproject.entity.User;
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
    List<Problem> findByVisibleTrue();
    
    /**
    * @Description: 通过id查找
    * @Param: [id]
    * @return: com.yw.ojproject.entity.Problem
    * @Author: YW
    * @Date: 
    */
    Problem findByidAndVisibleTrue(Integer id);
    
    /**
    * @Description: 通过tag查找
    * @Param: [tag]
    * @return: java.util.List<com.yw.ojproject.entity.Problem>
    * @Author: YW
    * @Date: 
    */
    List<Problem> findAllByTags(ProblemTag tag);
}
