package com.walker.test.exception;


public class CrazyCakeSerializationException extends Exception {
    public CrazyCakeSerializationException(String msg) {
        super(msg);
    }
    public CrazyCakeSerializationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
