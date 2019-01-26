package com.walker.test.exception;

public class CustomJedisException extends  RuntimeException{

    public CustomJedisException(String msg){
        super(msg);
    }
}
