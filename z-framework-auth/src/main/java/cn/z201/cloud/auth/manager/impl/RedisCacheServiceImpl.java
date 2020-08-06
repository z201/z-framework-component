package cn.z201.cloud.auth.manager.impl;

import com.google.gson.Gson;
import cn.z201.cloud.auth.manager.RedisCacheServiceI;
import cn.z201.cloud.auth.utils.JsonTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author z201.coding@gmail.com
 **/
@Service
@Slf4j
public class RedisCacheServiceImpl implements RedisCacheServiceI {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public Boolean setStr(String key, String value, Long timeout) {
        return (Boolean) redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            RedisSerializer stringSerializer = redisTemplate.getStringSerializer();
            return connection.setEx(
                    stringSerializer.serialize(key),
                    timeout,
                    stringSerializer.serialize(value));
        });
    }

    @Override
    public Boolean setStr(String key, String value) {
        return (Boolean) redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            RedisSerializer stringSerializer = redisTemplate.getStringSerializer();
            return connection.set(
                    stringSerializer.serialize(key),
                    stringSerializer.serialize(value));
        });
    }

    @Override
    public String getStr(String key) {
        return (String) redisTemplate.execute((RedisCallback<String>) connection -> {
            RedisSerializer stringSerializer = redisTemplate.getStringSerializer();
            byte[] value = connection.get(
                    stringSerializer.serialize(key));
            return (String) stringSerializer.deserialize(value);
        });
    }

    @Override
    public <T> T getStr(String key, Class<T> clazz) {
        return (T) redisTemplate.execute((RedisCallback<T>) connection -> {
            RedisSerializer<String> stringRedisSerializer = redisTemplate.getStringSerializer();
            byte[] bytes = connection.get(stringRedisSerializer.serialize(key));
            String json = stringRedisSerializer.deserialize(bytes);
            if (null == json) {
                return null;
            }
            Gson gson = new Gson();
            return gson.fromJson(json, clazz);
        });
    }

    @Override
    public <T, R> List<T> mGet(List<R> list, Class<T> clazz) {
        return (List<T>) redisTemplate.execute((RedisCallback<List<T>>) connection -> {
            byte[][] bkeys = new byte[list.size()][];
            for (int i = 0; i < list.size(); i++) {
                bkeys[i] = list.get(i).toString().getBytes();
            }
            RedisSerializer<String> stringRedisSerializer = redisTemplate.getStringSerializer();
            List<byte[]> byteList = connection.mGet(bkeys);
            List<T> result = new ArrayList<>();
            Gson gson = new Gson();
            for (int i = 0; i < byteList.size(); i++) {
                result.add(gson.fromJson(stringRedisSerializer.deserialize(byteList.get(i)), clazz));
            }
            return result;

        });
    }


    @Override
    public Long del(String key) {
        return (Long) redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer stringSerializer = redisTemplate.getStringSerializer();
            return connection.del(
                    stringSerializer.serialize(key));
        });
    }

    @Override
    public void hSet(String key, Map map) {
        redisTemplate.execute((RedisCallback) connection -> {
            RedisSerializer<String> stringRedisSerializer
                    = redisTemplate.getStringSerializer();
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            String mapKey;
            Object value;
            Map<byte[], byte[]> hashes = new LinkedHashMap(map.size());
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                mapKey = entry.getKey();
                value = entry.getValue();
                hashes.put(stringRedisSerializer.serialize(mapKey),
                        stringRedisSerializer.serialize(JsonTool.toJson(value)));
            }
            connection.hMSet(stringRedisSerializer.serialize(key),
                    hashes
            );
            return null;
        });
    }

    @Override
    public Object hGet(String key, String hashKey) {
        return redisTemplate.execute((RedisCallback) connection -> {
            RedisSerializer<String> stringRedisSerializer
                    = redisTemplate.getStringSerializer();
            byte[] bytes = connection.hGet(stringRedisSerializer.serialize(key),
                    stringRedisSerializer.serialize(hashKey));
            return stringRedisSerializer.deserialize(bytes);
        });
    }


}
