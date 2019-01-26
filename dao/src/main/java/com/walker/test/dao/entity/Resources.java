package com.walker.test.dao.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Resources implements Serializable {

    private static final long serialVersionUID = -6354713591058922869L;
    private int resId;
    private String resName;
    private String resPath;
    private String resDesc;
}
