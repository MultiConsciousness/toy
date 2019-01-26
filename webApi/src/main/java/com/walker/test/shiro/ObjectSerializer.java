package com.walker.test.shiro;

import com.walker.test.exception.CrazyCakeSerializationException;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class ObjectSerializer {

    public static final int BYTE_ARRAY_OUTPUT_STREAM_SIZE = 128;

    public byte[] serialize(Object object) throws CrazyCakeSerializationException {
        byte[] result = new byte[0];

        if (object == null) {
            return result;
        }
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(BYTE_ARRAY_OUTPUT_STREAM_SIZE);
        if (!(object instanceof Serializable)) {
            throw new CrazyCakeSerializationException("requires a Serializable payload "
                    + "but received an object of type [" + object.getClass().getName() + "]");
        }
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            result =  byteStream.toByteArray();
        } catch (IOException e) {
            throw new CrazyCakeSerializationException("serialize error, object=" + object, e);
        }

        return result;
    }

    public Object deserialize(byte[] bytes) throws CrazyCakeSerializationException {
        Object result = null;

        if (bytes == null || bytes.length == 0) {
            return result;
        }

        try {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteStream);
            result = objectInputStream.readObject();
        } catch (IOException e) {
            throw new CrazyCakeSerializationException("deserialize error", e);
        } catch (ClassNotFoundException e) {
            throw new CrazyCakeSerializationException("deserialize error", e);
        }

        return result;
    }
}
