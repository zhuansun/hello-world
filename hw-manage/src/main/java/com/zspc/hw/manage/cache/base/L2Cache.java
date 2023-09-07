package com.zspc.hw.manage.cache.base;

public interface L2Cache<K, V> {
    V get(K k);

    void remove(K k);

    void put(K k, V v);

    void putIfAbsent(K k, V v);
}
