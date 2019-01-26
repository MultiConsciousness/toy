package com.walker.test.shiro;

import com.walker.test.exception.CrazyCakeSerializationException;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class StringSerializer {

    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * Refer to https://docs.oracle.com/javase/8/docs/technotes/guides/intl/encoding.doc.html
     * UTF-8, UTF-16, UTF-32, ISO-8859-1, GBK, Big5, etc
     */
    private String charset = DEFAULT_CHARSET;

    public byte[] serialize(String s) throws CrazyCakeSerializationException {
        try {
            return (s == null ? null : s.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            throw new CrazyCakeSerializationException("serialize error, string=" + s, e);
        }
    }

    public String deserialize(byte[] bytes) throws CrazyCakeSerializationException {
        try {
            return (bytes == null ? null : new String(bytes, charset));
        } catch (UnsupportedEncodingException e) {
            throw new CrazyCakeSerializationException("deserialize error", e);
        }
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
