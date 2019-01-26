package com.walker.test.dao.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserPermission implements Serializable {
    private static final long serialVersionUID = 2943362354939140087L;
    private int userId;
    private int perId;
}
