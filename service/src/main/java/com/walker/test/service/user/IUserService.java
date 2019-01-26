package com.walker.test.service.user;

import com.walker.test.dao.entity.User;

public interface IUserService {
    User findUserByAccount(String account);
    int findUserIdByAccount(String account);
}
