package com.walker.test.shiro;

import com.walker.test.exception.CrazyCakeSerializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RedisSerializer {

    private static Logger log = LoggerFactory.getLogger(RedisSerializer.class);

    public byte[] stringSerialize(StringSerializer stringSerializer,String src){
        if(null == src || "".equals(src))
            return null;
        byte[] bytes = null;
        try {
            bytes = stringSerializer.serialize(src);
        } catch (CrazyCakeSerializationException e) {
            log.error(e.getMessage(),e);
            e.printStackTrace();
        }
        return bytes;
    }
    public String stringDeserialize(StringSerializer stringSerializer,byte[] src){
        if(null == src || src.length == 0)
            return null;
        String str = null;
        try {
            str = stringSerializer.deserialize(src);
        } catch (CrazyCakeSerializationException e) {
            log.error(e.getMessage(),e);
            e.printStackTrace();
        }
        return str;
    }

    public byte[] objectSerialize(ObjectSerializer objectSerializer,Object obj){
        if(null == obj)
            return null;
        byte[] bytes = null;
        try {
            bytes = objectSerializer.serialize(obj);
        } catch (CrazyCakeSerializationException e) {
            log.error(e.getMessage(),e);
            e.printStackTrace();
        }
        return bytes;
    }
    public Object objectDeserialize(ObjectSerializer objectSerializer,byte[] bytes){
        if(null == bytes || bytes.length == 0)
            return null;
        Object obj = null;
        try {
            obj = objectSerializer.deserialize(bytes);
        } catch (CrazyCakeSerializationException e) {
            log.error(e.getMessage(),e);
            e.printStackTrace();
        }
        return obj;
    }
}
