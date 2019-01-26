package com.walker.test.dao.mapper;

import com.walker.test.dao.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper mapper;

    @Test
    public void findAll() {
        List<User> users = mapper.findAll();
        Assert.assertEquals(1,users.size());
    }

    @Test
    public void findInfoByAccountTest(){
        User user = mapper.findInfoByAccount("test");
        Assert.assertEquals("1",user.getPassword());
    }

    @Test
    public void findUserIdByAccountTest(){
        int userId = mapper.findUserIdByAccount("test");
        Assert.assertEquals(1,userId);
    }

   }