package com.zspc.hw.manage.cache.base.redis.exception;

/**
 * redis操作异常
 */
public class RedisOperationException extends RuntimeException {
    public RedisOperationException() {
    }

    public RedisOperationException(String message) {
        super(message);
    }

    public RedisOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedisOperationException(Throwable cause) {
        super(cause);
    }

    public RedisOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public static void check(boolean expression, String message) {
        if (!expression) {
            throw new RedisOperationException(message);
        }
    }
}
