package com.walker.test.dao.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Permission implements Serializable {

    private static final long serialVersionUID = 7855052349205144947L;
    private int perId;
    private String perName;
    private String perDesc;
}
