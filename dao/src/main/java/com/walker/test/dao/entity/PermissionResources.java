package com.walker.test.dao.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class PermissionResources implements Serializable {

    private static final long serialVersionUID = 1253393192286238236L;
    private int perId;
    private int resId;
}
