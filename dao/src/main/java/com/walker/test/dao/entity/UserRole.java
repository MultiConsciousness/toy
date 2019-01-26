package com.walker.test.dao.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRole implements Serializable {
    private static final long serialVersionUID = -8256733578859466394L;
    private int userId;
    private int roleId;
}
