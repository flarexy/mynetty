package com.flare.netty.serialize;

import com.flare.netty.serialize.impl.JSONSerializer;

/**
 * @ClassName Serializer
 * @Description 序列化
 * @Author xieyi
 * @Data 2019/1/15
 */
public interface Serializer {

    /**
     * json 序列化
     */
    byte JSON_SERIALIZER = 1;

    Serializer DEFAULT = new JSONSerializer();

    /**
     * 序列化算法
     * @return
     */
    byte getSerializerAlgorithm();

    /**
     * java对象转成二进制
     * @param object
     * @return
     */
    byte[] serialize(Object object);

    /**
     * 二进制转java对象
     * @param clazz
     * @param bytes
     * @param <T>
     * @return
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
