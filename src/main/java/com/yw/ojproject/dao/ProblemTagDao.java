package com.yw.ojproject.dao;

import com.yw.ojproject.entity.ProblemTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProblemTagDao extends JpaRepository<ProblemTag, String>, JpaSpecificationExecutor<ProblemTag> {
    
    /**
    * @Description: 通过名字查找
    * @Param: [name]
    * @return: com.yw.ojproject.entity.ProblemTag
    * @Author: YW
    * @Date: 
    */
    ProblemTag findByName(String name);

}
