package com.walker.test.dao.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class RolePermission implements Serializable {

    private static final long serialVersionUID = 7910597053415418694L;
    private int roleId;
    private int perId;
}
