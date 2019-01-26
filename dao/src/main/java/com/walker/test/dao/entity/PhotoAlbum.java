package com.walker.test.dao.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PhotoAlbum implements Serializable {

    private static final long serialVersionUID = 6758380463873721189L;
    private int albumId;
    private int userId;
    private int available;
    private String albumName;
    private String desc;
    private Date createTime;
    private Date lastAccess;
    private int visible;
    private int encryption;
}
