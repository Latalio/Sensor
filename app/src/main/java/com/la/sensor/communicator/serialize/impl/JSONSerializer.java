package com.la.sensor.communicator.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.la.sensor.communicator.serialize.Serializer;
import com.la.sensor.communicator.serialize.SerializerAlgorithm;


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

        return JSON.parseObject(bytes, clazz);
    }
}
