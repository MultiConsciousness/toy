package com.walker.test.service.user.impl;

import com.walker.test.dao.entity.User;
import com.walker.test.dao.mapper.UserMapper;
import com.walker.test.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper mapper;

    @Override
    public User findUserByAccount(@NonNull String account) {
        User user = mapper.findInfoByAccount(account);
        return user;
    }

    @Override
    public int findUserIdByAccount(@NonNull String account) {
        return mapper.findUserIdByAccount(account);
    }
}
