package com.walker.test.service.user.impl;

import com.walker.test.dao.entity.User;
import com.walker.test.service.user.IUserService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImplTest {

    @Autowired
    private IUserService userService;

    @Test
    public void findUserByAccount() {
        if(null == userService)
            System.out.println("service is null");
        User user = userService.findUserByAccount("test");
        Assert.assertEquals("1",user.getPassword());
    }
}