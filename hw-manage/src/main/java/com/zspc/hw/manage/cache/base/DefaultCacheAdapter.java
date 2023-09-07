package com.zspc.hw.manage.cache.base;

import cn.hutool.core.lang.Assert;
import com.zspc.hw.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * 二级缓存模板
 * 数据库层面
 * 一级缓存：caffeine -> l1
 * 二级缓存：redis -> l2
 * 数据顺序 -> caffeine -> redis -> db
 */
@Slf4j
public class DefaultCacheAdapter<K, V> implements L2Cache<K, V> {
    public static final Long EXPIRE_TIME_ONE_HOUR = 60 * 60L;
    public static final Long EXPIRE_TIME_TEN_HOUR = 60 * 60L * 10;

    private final long l1TTLSeconds;
    private final long l2TTLSeconds;
    private final int l1MaxSize;
    private boolean isEnableL2;
    private boolean isCacheNull;
    private Function<K, V> finalGet;
    private final String l2KeyPrefix;
    private final AbstractL2Cache<K, V> cache;

    public DefaultCacheAdapter(Builder<K, V> builder) {
        Assert.notNull(builder.finalGet, "FinalGet can not be null");
        this.l1TTLSeconds = builder.l1TTLSeconds;
        this.l2TTLSeconds = builder.l2TTLSeconds;
        this.l1MaxSize = builder.l1MaxSize;
        this.isCacheNull = builder.isCacheNull;
        this.isEnableL2 = builder.isEnableL2;
        this.l2KeyPrefix = builder.l2KeyPrefix;
        this.finalGet = builder.finalGet;
        cache = new AbstractL2Cache<K, V>() {
            @Override
            protected String l2Key(K k) {
                Assert.notNull(l2KeyPrefix, "L2KeyPrefix can not be null");
                return l2KeyPrefix + k;
            }

            @Override
            protected long l1TTLSeconds() {
                return l1TTLSeconds;
            }

            @Override
            protected long l2TTLSeconds() {
                return l2TTLSeconds;
            }

            @Override
            protected int l1MaxSize() {
                return l1MaxSize;
            }

            @Override
            protected boolean isEnableL2() {
                return isEnableL2;
            }

            @Override
            protected boolean isCacheNull() {
                return isCacheNull;
            }

            @Override
            protected V finalGet(K k) {
                if (finalGet != null) {
                    return finalGet.apply(k);
                }
                return null;
            }
        };
    }

    @Override
    public V get(K k) {
        if (cache != null) {
            return cache.get(k);
        }
        throw BusinessException.get("get method is called, but cache is null");
    }

    @Override
    public void remove(K k) {
        if (cache != null) {
            cache.remove(k);
        }
    }

    @Override
    public void put(K k, V v) {
        if (cache != null) {
            cache.put(k, v);
        }
    }

    @Override
    public void putIfAbsent(K k, V v) {
        if (cache != null) {
            cache.putIfAbsent(k, v);
        }
    }

    public static class Builder<K, V> {
        private long l1TTLSeconds = -1;
        private long l2TTLSeconds = -1;
        private int l1MaxSize = -1;
        private boolean isEnableL2 = false;
        private boolean isCacheNull = false;
        private String l2KeyPrefix;
        private Function<K, V> finalGet;

        public Builder<K, V> setL1TTLSeconds(long l1TTLSeconds) {
            this.l1TTLSeconds = l1TTLSeconds;
            return this;
        }

        public Builder<K, V> setL2TTLSeconds(long l2TTLSeconds) {
            this.l2TTLSeconds = l2TTLSeconds;
            return this;
        }

        public Builder<K, V> setL1MaxSize(int l1MaxSize) {
            this.l1MaxSize = l1MaxSize;
            return this;
        }

        public Builder<K, V> setEnableL2(boolean enableL2) {
            isEnableL2 = enableL2;
            return this;
        }

        public Builder<K, V> setCacheNull(boolean cacheNull) {
            isCacheNull = cacheNull;
            return this;
        }

        public Builder<K, V> setFinalGet(Function<K, V> finalGet) {
            this.finalGet = finalGet;
            return this;
        }

        public Builder<K, V> setL2KeyPrefix(String l2KeyPrefix) {
            this.l2KeyPrefix = l2KeyPrefix;
            return this;
        }

        public DefaultCacheAdapter<K, V> build() {
            return new DefaultCacheAdapter<>(this);
        }
    }
}


