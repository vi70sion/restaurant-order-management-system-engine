package org.example.service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.Set;

public class RedisService {

    private final JedisPool jedisPool;

    public RedisService(String host, int port) {
        this.jedisPool = new JedisPool(host, port);
    }

    public Set<String> getKeys() {
        Jedis jedis = jedisPool.getResource();
        Set<String> keys = jedis.keys("*");
        return keys;
    }

//    // Išsaugome String vertę tiesiogiai
//    public void put(String key, String numbersAsJson) {
//        try (Jedis jedis = jedisPool.getResource()) {
//            jedis.set(key, numbersAsJson);  // Nereikia serializuoti
//        }
//    }

    public void putWithExpiration(String key, Object value, int seconds) throws IOException {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.setex(key.getBytes(), seconds, serialize(value));
        }
    }


    // Gauname String vertę tiesiogiai
    public String get(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);  // Nereikia deserializuoti
        }
    }


    private byte[] serialize(Object obj) throws IOException {
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(byteOut)) {
            out.writeObject(obj);
            return byteOut.toByteArray();
        }
    }

    private Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream byteIn = new ByteArrayInputStream(data);
             ObjectInputStream in = new ObjectInputStream(byteIn)) {
            return in.readObject();
        }
    }


    public void close() {
        jedisPool.close();
    }
}