package com.walker.test.service.user;

import com.walker.test.dao.entity.Role;

import java.util.List;

public interface IRoleService {

    Role findRoleByUserId(int userId);
}
