package com.walker.test.dao.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = 5054108262656891975L;
    private int userId;
    private String account;
    private String password;
    private String salt;
    private String email;
    private String nickName;
    private Date createTime;
    private Date lastLogin;
    private Date modifyTime;
    private int lock;
}
