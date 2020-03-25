package com.yw.ojproject.dao;

import com.yw.ojproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserDao extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    /**
    * @Description: 登陆
    * @Param: [username, password]
    * @return: com.yw.ojproject.entity.User
    * @Author: YW
    * @Date:
    */
    User findByUsernameAndPassword(String username, String password);

    /**
    * @Description: 查重
    * @Param: [username, email]
    * @return: bool
    * @Author: YW
    * @Date:
    */
    Integer countByUsername(String username);

    /**
    * @Description: 查重
    * @Param: [email]
    * @return: java.lang.Integer
    * @Author: YW
    * @Date:
    */
    Integer countByEmail(String email);
}
