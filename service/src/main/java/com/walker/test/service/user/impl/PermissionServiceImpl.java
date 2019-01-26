package com.walker.test.service.user.impl;

import com.walker.test.common.SysConstant;
import com.walker.test.dao.mapper.PermissionMapper;
import com.walker.test.service.user.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionServiceImpl implements IPermissionService {

    @Autowired
    private PermissionMapper mapper;

    @Override
    public List<String> findPersByUserId(int userId) {
        List<String> pers = new ArrayList<>();
        if(SysConstant.IllegalArgument.ILLEGAL_ID != userId)
            pers = mapper.findPersByUserId(userId);
        return pers;
    }

    @Override
    public List<String> findPersUnderRoleByUserId(int userId) {
        List<String> pers = new ArrayList<>();
        if(SysConstant.IllegalArgument.ILLEGAL_ID != userId)
            pers = mapper.findPersUnderRoleIdByUserId(userId);
        return pers;
    }
}
