package com.lcc.ucbackend.service;


import com.lcc.ucbackend.model.domain.User;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


/**
 * 用户服务测试
 */
@SpringBootTest
class UserServiceTest {
    @Resource
    private UserService userService;

    @Test
    void testAddUser() {
        User user = new User();
        user.setUserName("1");
        user.setUserAccount("2");
        user.setAvatarUrl("1");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhone("16");
        user.setEmail("1");
        user.setUserRole(0);
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assert.assertTrue(result);
    }

    @Test
    void userRegister() {
        String userAccount = "lccc";
        String userPassword = "";
        String checkPassword = "12345678";
        String planetCode ="1";
        long result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1, result);
        userAccount = "ue";
        result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1, result);
        userAccount = "lccc";
        userPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1, result);
        userAccount = "llc lc";
        result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1, result);
        userPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1, result);
        userAccount = "2";
        userPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1, result);
        userAccount = "lcccc";
        userPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertTrue(result > 0);
    }
}