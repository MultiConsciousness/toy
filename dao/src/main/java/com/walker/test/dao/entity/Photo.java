package com.walker.test.dao.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Photo implements Serializable {

    private static final long serialVersionUID = 6128926244312650972L;
    private int photoId;
    private int albumId;
    private String desc;
    private Date uploadTime;
    private String uri;
}
