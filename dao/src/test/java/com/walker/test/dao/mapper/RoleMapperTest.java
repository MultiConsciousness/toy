package com.walker.test.dao.mapper;

import com.walker.test.dao.entity.Role;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleMapperTest {

    @Autowired
    private RoleMapper mapper;

    @Test
    public void findAll() {
        List<Role> roles = mapper.findAll();
        Assert.assertEquals(1,roles.size());
    }

    @Test
    public void findRoleIdByUserId() {
        Role role = mapper.findRoleNameByUserId(1);
        Assert.assertEquals("admin",role.getRoleName());
    }

}