package com.flare.netty.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.flare.netty.serialize.Serializer;
import com.flare.netty.serialize.SerializerAlgorithm;

/**
 * @ClassName JSONSerializer
 * @Description json序列化
 * @Author xieyi
 * @Data 2019/1/15
 */
public class JSONSerializer implements Serializer {

    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes,clazz);
    }
}
