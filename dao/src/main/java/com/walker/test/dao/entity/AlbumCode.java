package com.walker.test.dao.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class AlbumCode implements Serializable{

    private static final long serialVersionUID = -7309014751554086907L;
    private int codeId;
    private int albumId;
    private String question;
    private String answer;
}
