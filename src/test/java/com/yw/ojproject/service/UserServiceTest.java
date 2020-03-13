package com.yw.ojproject.service;

import com.yw.ojproject.dto.ReturnData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserServer userServer;

    @Test
    void userRegister() {
        ReturnData rd = userServer.userRegister("root", "root", "root@qq.com");
        Assert.state((((String)rd.getData()).compareTo("Succeed") != 0), "wrong");
    }

    @Test
    void userLogin() {
        //ReturnData rd = userService.userLogin("root", "root", )
    }

    @Test
    void userLogout() {
    }

    @Test
    void checkUsernameOrEmail() {
        ReturnData rd = userServer.checkUsernameOrEmail("root@root.com", "");
        Assert.state((((String)rd.getData()).compareTo("Succeed") != 0), "wrong");
    }

    @Test
    void changeEmail() {
        //ReturnData rd = userService.changeEmail("root", "root@root.com", "root@qq.com")
    }

    @Test
    void changePassword() {
    }
}