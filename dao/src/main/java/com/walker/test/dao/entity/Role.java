package com.walker.test.dao.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Role implements Serializable {

    private static final long serialVersionUID = -9115775476621436122L;
    private int roleId;
    private String roleName;
    private String roleDesc;
}
