package com.walker.test.dao.mapper;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PermissionMapperTest {

    @Autowired
    private PermissionMapper mapper;

    @Test
    public void findPersUnderRoleIdByUserId() {
        List<String> perNames = mapper.findPersUnderRoleIdByUserId(1);
        Assert.assertEquals(3,perNames.size());
    }

    @Test
    public void findPersByUserId(){
        List<String> pers = mapper.findPersByUserId(1);
        Assert.assertEquals(2,pers.size());
    }

}