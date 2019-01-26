package com.walker.test.service.user;


import java.util.List;

public interface IPermissionService {

    List<String> findPersByUserId(int userId);
    List<String> findPersUnderRoleByUserId(int userId);
}
