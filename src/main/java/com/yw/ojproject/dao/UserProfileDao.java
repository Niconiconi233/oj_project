package com.yw.ojproject.dao;

import com.yw.ojproject.entity.User;
import com.yw.ojproject.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserProfileDao extends JpaRepository<UserProfile, String>, JpaSpecificationExecutor<UserProfile> {
    /**
    * @Description: 通过用户查询
    * @Param: [user]
    * @return: com.yw.ojproject.entity.UserProfile
    * @Author: YW
    * @Date: 
    */
    public UserProfile findByUser(User user);

}
