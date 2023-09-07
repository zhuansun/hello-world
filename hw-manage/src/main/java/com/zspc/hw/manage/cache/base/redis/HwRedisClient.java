package com.zspc.hw.manage.cache.base.redis;

import java.util.List;
import java.util.Set;

public interface HwRedisClient {

    /**
     * 判断key是否存在
     *
     * @param key key
     * @return 是否存在
     */
    boolean exists(String key);

    /**
     * 删除key
     *
     * @param keys keys
     * @return 是否成功
     */
    boolean delete(String... keys);

    /**
     * 删除key
     *
     * @param key key
     * @return 是否成功
     */
    boolean delete(String key);

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return 是否设置成功
     */
    boolean expire(String key, long time);

    boolean expireMill(String key, long time);

    /**
     * 获取过期时间
     *
     * @param key key
     * @return 过期时间秒
     */
    long getExpire(String key);

    /**
     * 递增
     *
     * @param key key
     * @param i   step
     * @return 递增值
     */
    long incrby(String key, long i);

    /**
     * 递减
     *
     * @param key key
     * @param i   step
     * @return 递减
     */
    long decrby(String key, long i);

    // ==== value操作 ====

    /**
     * * 值操作 写入
     *
     * @param key   key
     * @param value value
     * @param <T>   类型
     * @return 是否成功
     */
    <T> boolean set(String key, T value);

    /**
     * 值操作 写入并设置过期时间(单位秒)
     *
     * @param key        key
     * @param value      value
     * @param expireTime expireTime
     * @param <T>        类型
     * @return 是否成功
     */
    <T> boolean set(String key, T value, Long expireTime);

    /**
     * 不存在则设置
     *
     * @param key   key
     * @param value value
     * @param <T>   类型
     * @return 是否成功
     */
    <T> boolean setIfNotExist(String key, T value);

    /**
     * 不存在则设置
     *
     * @param key        key
     * @param value      value
     * @param expireTime 过期时间
     * @param <T>        类型
     * @return 是否成功
     */
    <T> boolean setIfNotExist(String key, T value, Long expireTime);

    /**
     * 获取value
     *
     * @param key key
     * @return 返回值对象
     */
    Object get(String key);

    /**
     * 获取value
     *
     * @param key   key
     * @param clazz 类型
     * @return 返回对应类型对象
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 获取value 设置过期时间表示延长过期时间
     *
     * @param key    key
     * @param clazz  类型
     * @param expire 过期时间
     * @return 对应类型对象
     */
    @Deprecated
    <T> T get(String key, Class<T> clazz, long expire);

    /**
     * 设置二进制值
     *
     * @param key   key
     * @param bytes 二进制值
     * @return 是否成功
     */
    boolean setBytes(String key, byte[] bytes);

    /**
     * 设置二进制
     *
     * @param key    key
     * @param bytes  二进制数组
     * @param expire 过期时间
     * @return 是否成功
     */
    boolean setBytes(String key, byte[] bytes, long expire);

    /**
     * 获取二进制值
     *
     * @param key key
     * @return 值
     */
    byte[] getBytes(String key);

    // ==== hash操作 ====

    /**
     * 哈希 添加
     *
     * @param key     key
     * @param hashKey hash key
     * @param value   hash value
     * @return 是否成功
     */
    <T> boolean hSet(String key, String hashKey, T value);

    /**
     * 哈希获取数据
     *
     * @param key     key
     * @param hashKey hash key
     * @param clazz   类型
     * @return 值
     */
    <T> T hGet(String key, String hashKey, Class<T> clazz);

    /**
     * 删除对应key的值
     *
     * @param key     key
     * @param hashKey hashKey
     * @return 是否成功
     */
    boolean hDel(String key, String hashKey);

    /**
     * 判断对应key是否存在
     *
     * @param key     key
     * @param hashKey hash key
     * @return 是否成功
     */
    boolean hExists(String key, String hashKey);

    /**
     * hash中元素数量
     *
     * @param key key
     * @return 长度大小
     */
    long hLen(String key);

    // ==== list操作 ====

    /**
     * 列表添加
     *
     * @param key   key
     * @param value value
     * @param <T>   类型
     * @return 是否添加成功
     */
    <T> long lrPush(String key, T value);

    /**
     * 列表添加一批数据
     *
     * @param key    key
     * @param values values
     * @param <T>    类型
     */
    <T> void lrPushAll(String key, List<T> values);

    /**
     * 获取index元素
     *
     * @param key   key
     * @param index 索引值
     * @param clazz 类型
     * @return 对象
     */
    <T> T lIndex(String key, long index, Class<T> clazz);

    /**
     * 查询list中一段数据
     *
     * @param key   key
     * @param start start
     * @param end   end
     * @param clazz 类型
     * @return 集合
     */
    <T> List<T> lRange(String key, long start, long end, Class<T> clazz);

    /**
     * list长度
     *
     * @param key key
     * @return 长度
     */
    long lLen(String key);

    /**
     * 设置某个索引的值
     *
     * @param key   key
     * @param index 索引
     * @param value 值
     * @param <T>   类型
     * @return 是否成功
     */
    <T> T lSet(String key, long index, T value);


    // ==== set操作 ====

    /**
     * 集合添加
     *
     * @param key   key
     * @param value value
     * @return 对象
     */
    <T> long sAdd(String key, T value);

    /**
     * 获取集合中说有元素
     *
     * @param key   key
     * @param clazz 类型
     * @return 集合
     */
    <T> Set<T> sMembers(String key, Class<T> clazz);

    /**
     * 删除并获取一个元素
     *
     * @param key   key
     * @param clazz 类型
     * @param <T>   类型
     * @return 对象
     */
    <T> T sPop(String key, Class<T> clazz);

    /**
     * 判断是否存在集合中
     *
     * @param key   key
     * @param value value
     * @param <T>   类型
     * @return 是否存在
     */
    <T> boolean sIsMember(String key, T value);

    /**
     * 集合元素数量
     *
     * @param key key
     * @return 数量
     */
    long sLen(String key);

    // ==== sort set 操作 ====

    /**
     * 有序集合添加
     *
     * @param key   key
     * @param value value
     * @param score 分数
     * @return 是否成功
     */
    boolean zAdd(String key, Object value, double score);

    /**
     * 有序集合获取
     *
     * @param key      key
     * @param minScore 最小分数
     * @param maxScore 最大分数
     * @param clazz    类型
     * @return 集合
     */
    <T> Set<T> zRangeByScore(String key, double minScore, double maxScore, Class<T> clazz);

    /**
     * 获取成员分数
     *
     * @param key   key
     * @param value value
     * @param <T>   类型
     * @return 分数
     */
    <T> double zScore(String key, T value);

    /**
     * set size
     *
     * @param key key
     * @return 集合大小
     */
    long zLen(String key);

    // ==== lock 操作 ====

    /**
     * 加锁
     *
     * @param key        key
     * @param expireTime 过期时间(秒) -1 不过期
     * @return 是否成功
     * @throws InterruptedException 打断
     */
    boolean tryLock(String key, long expireTime) throws InterruptedException;

    boolean tryLockMill(String key, long expireTime) throws InterruptedException;

    /**
     * 加锁
     *
     * @param key        key
     * @param waitTime   等待时间
     * @param expireTime 过期时间(秒) -1 不过期
     * @return 是否成功
     * @throws InterruptedException 打断
     */
    boolean tryLock(String key, long waitTime, long expireTime) throws InterruptedException;

    boolean tryLockMill(String key, long waitTime, long expireTime) throws InterruptedException;

    /**
     * 释放锁
     *
     * @param key key
     * @return 是否成功
     */
    boolean unlock(String key);

    /**
     * 强制解锁
     *
     * @param key key
     */
    void forceUnlock(String key);
}
