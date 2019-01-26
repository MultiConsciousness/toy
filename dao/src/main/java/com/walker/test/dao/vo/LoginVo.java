package com.walker.test.dao.vo;

import lombok.Data;

@Data
public class LoginVo {

    private String account;
    private String password;
    private boolean rememberMe;

}
