package com.anji.captcha.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 本地缓存实现。
 */
public final class CacheUtil {

    private static final Map<String, Object> CACHE_MAP = new ConcurrentHashMap<String, Object>();

    private static Integer CACHE_MAX_NUMBER = Integer.valueOf(1000);

    private static ScheduledExecutorService scheduledExecutor;

    private CacheUtil() {
    }

    /**
     * 初始化缓存。
     *
     * @param maxNumber 最大缓存条数
     * @param timingClearSecond 清理间隔，单位秒
     */
    public static synchronized void init(int maxNumber, long timingClearSecond) {
        CACHE_MAX_NUMBER = Integer.valueOf(maxNumber);
        if (timingClearSecond <= 0L || scheduledExecutor != null) {
            return;
        }
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            private final AtomicInteger counter = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable);
                thread.setName("captcha-cache-cleaner-" + counter.getAndIncrement());
                thread.setDaemon(true);
                return thread;
            }
        });
        scheduledExecutor = executor;
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                refresh();
            }
        }, 10L, timingClearSecond, TimeUnit.SECONDS);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                ScheduledExecutorService executorService = scheduledExecutor;
                if (executorService != null) {
                    executorService.shutdownNow();
                }
            }
        }));
    }

    /**
     * 刷新过期缓存。
     */
    public static void refresh() {
        for (String key : CACHE_MAP.keySet()) {
            exists(key);
        }
    }

    /**
     * 写入缓存。
     *
     * @param key 缓存键
     * @param value 缓存值
     * @param expiresInSeconds 过期时间
     */
    public static void set(String key, String value, long expiresInSeconds) {
        if (CACHE_MAP.size() > CACHE_MAX_NUMBER.intValue() * 2) {
            clear();
        }
        CACHE_MAP.put(key, value);
        if (expiresInSeconds > 0L) {
            CACHE_MAP.put(key + "_HoldTime", Long.valueOf(System.currentTimeMillis() + expiresInSeconds * 1000L));
        }
    }

    /**
     * 删除缓存。
     *
     * @param key 缓存键
     */
    public static void delete(String key) {
        CACHE_MAP.remove(key);
        CACHE_MAP.remove(key + "_HoldTime");
    }

    /**
     * 判断缓存是否存在。
     *
     * @param key 缓存键
     * @return true=存在
     */
    public static boolean exists(String key) {
        Long holdTime = (Long) CACHE_MAP.get(key + "_HoldTime");
        if (holdTime == null || holdTime.longValue() == 0L) {
            return false;
        }
        if (holdTime.longValue() < System.currentTimeMillis()) {
            delete(key);
            return false;
        }
        return true;
    }

    /**
     * 获取缓存值。
     *
     * @param key 缓存键
     * @return 缓存值
     */
    public static String get(String key) {
        if (exists(key)) {
            return (String) CACHE_MAP.get(key);
        }
        return null;
    }

    /**
     * 清空缓存。
     */
    public static void clear() {
        CACHE_MAP.clear();
    }

    /**
     * 更新过期时间。
     *
     * @param key 缓存键
     * @param expiresInSeconds 过期时间
     */
    public static void setExpire(String key, long expiresInSeconds) {
        CACHE_MAP.put(key + "_HoldTime", Long.valueOf(System.currentTimeMillis() + expiresInSeconds * 1000L));
    }
}
