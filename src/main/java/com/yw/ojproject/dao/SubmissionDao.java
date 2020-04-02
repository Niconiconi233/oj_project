package com.yw.ojproject.dao;
import	java.util.List;

import com.yw.ojproject.entity.Problem;
import com.yw.ojproject.entity.Submission;
import com.yw.ojproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SubmissionDao extends JpaRepository<Submission, String>, JpaSpecificationExecutor<Submission> {

    /**
    * @Description: 通过problem查找记录集合
    * @Param: [problem]
    * @return: java.util.List<com.yw.ojproject.entity.Submission>
    * @Author: YW
    * @Date:
    */
    List<Submission> findAllByProblemOrderByCtimeDesc(Problem problem);
    
    /**
    * @Description: 通过user查找记录集合
    * @Param: [user]
    * @return: java.util.List<com.yw.ojproject.entity.Submission>
    * @Author: YW
    * @Date: 
    */
    List<Submission> findAllByUserOrderByCtimeDesc(User user);
}
