package com.walker.test.service.user.impl;

import com.walker.test.common.SysConstant;
import com.walker.test.dao.entity.Role;
import com.walker.test.dao.mapper.RoleMapper;
import com.walker.test.service.user.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private RoleMapper mapper;

    @Override
    public Role findRoleByUserId(int userId) {
        Role role = new Role();
        if(SysConstant.IllegalArgument.ILLEGAL_ID != userId) {
            role = mapper.findRoleNameByUserId(userId);
        }
        return role;
    }
}

