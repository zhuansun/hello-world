package com.zspc.hw.manage.cache.base.redis.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.zspc.hw.common.constant.Constants;
import com.zspc.hw.manage.cache.base.redis.HwRedisClient;
import com.zspc.hw.manage.cache.base.redis.exception.RedisOperationException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class HwRedisClientImpl implements HwRedisClient {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public boolean exists(String key) {
        return ObjectUtil.defaultIfNull(this.redisTemplate.hasKey(key), false);
    }

    @Override
    public boolean delete(String... keys) {
        if (keys == null || keys.length == 0) {
            return true;
        }
        Long delete = this.redisTemplate.delete(Arrays.asList(keys));
        return delete != null && delete != 0;
    }

    @Override
    public boolean delete(String key) {
        return ObjectUtil.defaultIfNull(this.redisTemplate.delete(key), false);
    }

    @Override
    public boolean expire(String key, long time) {
        // 不允许设置未永久
        time = time <= 0 ? Constants.DEFAULT_EPXIRE : time;
        return ObjectUtil.defaultIfNull(this.redisTemplate.expire(key, time, TimeUnit.SECONDS), false);
    }

    @Override
    public boolean expireMill(String key, long time) {
        // 不允许设置未永久
        time = time <= 0 ? Constants.DEFAULT_EPXIRE_MILL : time;
        return ObjectUtil.defaultIfNull(this.redisTemplate.expire(key, time, TimeUnit.MILLISECONDS), false);
    }

    @Override
    public long getExpire(String key) {
        // -2 代表不存在key
        // -1 代表永久不过期
        return ObjectUtil.defaultIfNull(this.redisTemplate.getExpire(key), -2L);
    }

    @Override
    public long incrby(final String key, final long i) {
        List<Object> objects = this.redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.opsForValue().increment(key, i);
                operations.getExpire(key);
                return null;
            }
        });
        Long increment = (Long) objects.get(0);
        RedisOperationException.check(increment != null, "it is not possible!");
        this.checkExpireTime(key, (Long) objects.get(1));
        return increment;
    }

    @Override
    public long decrby(final String key, final long i) {
        List<Object> objects = this.redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.opsForValue().decrement(key, i);
                operations.getExpire(key);
                return null;
            }
        });
        Long decrement = (Long) objects.get(0);
        RedisOperationException.check(decrement != null, "it is not possible!");
        this.checkExpireTime(key, (Long) objects.get(1));
        return decrement;
    }

    @Override
    public <T> boolean set(String key, T value) {
        return this.set(key, value, Constants.DEFAULT_EPXIRE);
    }

    @Override
    public <T> boolean set(String key, T value, Long expireTime) {
        RedisOperationException.check(expireTime != null && expireTime > 0, "expire time must greater than 0!");
        this.redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
        return true;
    }

    @Override
    public <T> boolean setIfNotExist(String key, T value) {
        return this.setIfNotExist(key, value, Constants.DEFAULT_EPXIRE);
    }

    @Override
    public <T> boolean setIfNotExist(String key, T value, Long expireTime) {
        RedisOperationException.check(expireTime != null && expireTime > 0, "expire time must greater than 0!");
        Boolean result = this.redisTemplate.opsForValue().setIfAbsent(key, value, expireTime, TimeUnit.SECONDS);
        return ObjectUtil.defaultIfNull(result, false);
    }

    @Override
    public Object get(String key) {
        Object value = this.redisTemplate.opsForValue().get(key);
        this.checkValueExists(key, value);
        return value;
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        return clazz.cast(this.get(key));
    }

    @Override
    public <T> T get(String key, Class<T> clazz, long expire) {
        return this.get(key,clazz);
    }

    @Override
    public boolean setBytes(String key, byte[] bytes) {
        return this.setBytes(key, bytes, Constants.DEFAULT_EPXIRE);
    }

    @Override
    public boolean setBytes(String key, byte[] bytes, long expire) {
        RedisOperationException.check(expire > 0, "expire time must greater than 0!");
        this.redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.set(redisTemplate.getStringSerializer().serialize(key), bytes, Expiration.seconds(expire), RedisStringCommands.SetOption.UPSERT);
            }
        });
        return true;
    }

    @Override
    public byte[] getBytes(String key) {
        byte[] bytes = this.redisTemplate.execute(new RedisCallback<byte[]>() {
            @Override
            public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.get(redisTemplate.getStringSerializer().serialize(key));
            }
        });
        this.checkValueExists(key, bytes);
        return bytes;
    }

    @Override
    public <T> boolean hSet(String key, String hashKey, T value) {
        List<Object> objects = this.redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.hasKey(key);
                operations.opsForHash().put(key, hashKey, value);
                operations.getExpire(key);
                return null;
            }
        });
        boolean exists = objects.get(0) == null ? false : (Boolean) objects.get(0);
        if (!exists) {
            this.checkExpireTime(key, (Long) objects.get(2));
        }
        return true;
    }

    @Override
    public <T> T hGet(String key, String hashKey, Class<T> clazz) {
        return clazz.cast(this.redisTemplate.opsForHash().get(key, hashKey));
    }

    @Override
    public boolean hDel(String key, String hashKey) {
        Long delete = this.redisTemplate.opsForHash().delete(key, hashKey);
        return delete > 0;
    }

    @Override
    public boolean hExists(String key, String hashKey) {
        return this.redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    @Override
    public long hLen(String key) {
        return this.redisTemplate.opsForHash().size(key);
    }

    @Override
    public <T> long lrPush(String key, T value) {
        List<Object> objects = this.redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.hasKey(key);
                operations.opsForList().rightPush(key, value);
                operations.getExpire(key);
                return null;
            }
        });
        boolean exists = objects.get(0) == null ? false : (Boolean) objects.get(0);
        Long index = (Long) objects.get(1);
        if (!exists) {
            this.checkExpireTime(key, (Long) objects.get(2));
        }
        return index;
    }

    @Override
    public <T> void lrPushAll(String key, List<T> values) {
        if (CollectionUtil.isNotEmpty(values)) {
            List<Object> objects = this.redisTemplate.executePipelined(new SessionCallback<Object>() {
                @Override
                public Object execute(RedisOperations operations) throws DataAccessException {
                    operations.hasKey(key);
                    operations.opsForList().rightPushAll(key, values);
                    operations.getExpire(key);
                    return null;
                }
            });
            boolean exists = objects.get(0) == null ? false : (Boolean) objects.get(0);
            // 这个地方需要分开写且不能加泛型, 不然重载方法定位不到正确的方法
            if (!exists) {
                this.checkExpireTime(key, (Long) objects.get(2));
            }
        }
    }

    @Override
    public <T> T lIndex(String key, long index, Class<T> clazz) {
        return clazz.cast(this.redisTemplate.opsForList().index(key, index));
    }

    @Override
    public <T> List<T> lRange(String key, long start, long end, Class<T> clazz) {
        List<Object> range = this.redisTemplate.opsForList().range(key, start, end);
        range = ObjectUtil.defaultIfNull(range, new ArrayList<>());
        return range.stream().map(clazz::cast).collect(Collectors.toList());
    }

    @Override
    public long lLen(String key) {
        return ObjectUtil.defaultIfNull(this.redisTemplate.opsForList().size(key), 0L);
    }

    @Override
    public <T> T lSet(String key, long index, T value) {
        List<Object> objects = this.redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.opsForList().index(key, index);
                operations.opsForList().set(key, index, value);
                return null;
            }
        });
        return (T) objects.get(0);
    }

    @Override
    public <T> long sAdd(String key, T value) {
        List<Object> objects = this.redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.hasKey(key);
                operations.opsForSet().add(key, value);
                operations.getExpire(key);
                return null;
            }
        });

        boolean exists = objects.get(0) == null ? false : (Boolean) objects.get(0);
        Long add = (Long) objects.get(1);
        if (!exists) {
            this.checkExpireTime(key, (Long) objects.get(2));
        }
        return ObjectUtil.defaultIfNull(add, 1L);
    }

    @Override
    public <T> Set<T> sMembers(String key, Class<T> clazz) {
        Set<Object> members = this.redisTemplate.opsForSet().members(key);
        return ObjectUtil.defaultIfNull(members, new HashSet<>()).stream().map(clazz::cast).collect(Collectors.toSet());
    }

    @Override
    public <T> T sPop(String key, Class<T> clazz) {
        Object pop = this.redisTemplate.opsForSet().pop(key);
        return clazz.cast(pop);
    }

    @Override
    public <T> boolean sIsMember(String key, T value) {
        return ObjectUtil.defaultIfNull(this.redisTemplate.opsForSet().isMember(key, value), false);
    }

    @Override
    public long sLen(String key) {
        return ObjectUtil.defaultIfNull(this.redisTemplate.opsForSet().size(key), 0L);
    }

    @Override
    public boolean zAdd(String key, Object value, double score) {
        List<Object> objects = this.redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.hasKey(key);
                operations.opsForZSet().add(key, value, score);
                operations.getExpire(key);
                return null;
            }
        });
        boolean exists = objects.get(0) == null ? false : (Boolean) objects.get(0);
        Boolean add = (Boolean) objects.get(1);
        if (!exists) {
            this.checkExpireTime(key, (Long) objects.get(2));
        }
        return ObjectUtil.defaultIfNull(add, false);
    }

    @Override
    public <T> Set<T> zRangeByScore(String key, double minScore, double maxScore, Class<T> clazz) {
        Set<Object> range = this.redisTemplate.opsForZSet().rangeByScore(key, minScore, maxScore);
        return ObjectUtil.defaultIfNull(range, new HashSet<>()).stream().map(clazz::cast).collect(Collectors.toSet());
    }

    @Override
    public <T> double zScore(String key, T value) {
        Double score = this.redisTemplate.opsForZSet().score(key, value);
        return ObjectUtil.defaultIfNull(score, 0d);
    }

    @Override
    public long zLen(String key) {
        return ObjectUtil.defaultIfNull(this.redisTemplate.opsForZSet().size(key), 0L);
    }


    @Override
    public boolean tryLock(String key, long expireTime) throws InterruptedException {
        return this.tryLock(key, 0L, expireTime);
    }

    @Override
    public boolean tryLockMill(String key, long expireTime) throws InterruptedException {
        return this.tryLockMill(key, 0L, expireTime);

    }

    @Override
    public boolean tryLock(String key, long waitTime, long expireTime) throws InterruptedException {
        RLock lock = this.redissonClient.getLock(key);
        return lock.tryLock(waitTime, expireTime, TimeUnit.SECONDS);
    }

    @Override
    public boolean tryLockMill(String key, long waitTime, long expireTime) throws InterruptedException {
        RLock lock = this.redissonClient.getLock(key);
        return lock.tryLock(waitTime, expireTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean unlock(String key) {
        RLock lock = this.redissonClient.getLock(key);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
        return true;
    }

    @Override
    public void forceUnlock(String key) {
        RLock lock = this.redissonClient.getLock(key);
        lock.forceUnlock();
    }

    private void checkExpireTime(String key, Long expire) {
        if (expire == null || expire < 0) {
            log.warn("key:{} does not set expire time,please set expire time manually!", key);
        }
    }

    private void checkValueExists(String key, Object value) {
        if (value == null) {
            log.warn("key:{} is not eixsts", key);
        }
    }
}
