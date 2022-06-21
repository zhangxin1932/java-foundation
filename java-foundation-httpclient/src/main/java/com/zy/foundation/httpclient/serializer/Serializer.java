package com.zy.foundation.httpclient.serializer;

public interface Serializer {
    <T> byte[] serializer(T value);

    <T> T deserializer(byte[] bytes, Class<T> returnType);

    <T> T deserializer(byte[] bytes);
}
