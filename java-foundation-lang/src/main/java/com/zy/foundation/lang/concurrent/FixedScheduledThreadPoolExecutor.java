package com.zy.foundation.lang.concurrent;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 经过修正后的 ScheduledThreadPoolExecutor, 防止无限制的创建线程, 因为默认的最大线程数是: Integer.MAX_VALUE
 * 同时限制了 阻塞队列的 size 大小, 超过即阻塞
 */
public class FixedScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor {

    public static final String DEFAULT_MAX_BLOCKING_QUEUE_SIZE = "4096";
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private final String blockingQueueSizeLimitKey;
    private final Long awaitTime;

    public FixedScheduledThreadPoolExecutor(String blockingQueueSizeLimitKey, int corePoolSize, Long awaitTime) {
        super(corePoolSize);
        this.blockingQueueSizeLimitKey = blockingQueueSizeLimitKey;
        this.awaitTime = awaitTime;
        this.setMaximumPoolSize(corePoolSize);
    }

    public FixedScheduledThreadPoolExecutor(String blockingQueueSizeLimitKey, int corePoolSize, ThreadFactory threadFactory, Long awaitTime) {
        super(corePoolSize, threadFactory);
        this.blockingQueueSizeLimitKey = blockingQueueSizeLimitKey;
        this.awaitTime = awaitTime;
        this.setMaximumPoolSize(corePoolSize);
    }

    public FixedScheduledThreadPoolExecutor(String blockingQueueSizeLimitKey, int corePoolSize, RejectedExecutionHandler handler, Long awaitTime) {
        super(corePoolSize, handler);
        this.blockingQueueSizeLimitKey = blockingQueueSizeLimitKey;
        this.awaitTime = awaitTime;
        this.setMaximumPoolSize(corePoolSize);
    }

    public FixedScheduledThreadPoolExecutor(String blockingQueueSizeLimitKey, int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler, Long awaitTime) {
        super(corePoolSize, threadFactory, handler);
        this.blockingQueueSizeLimitKey = blockingQueueSizeLimitKey;
        this.awaitTime = awaitTime;
        this.setMaximumPoolSize(corePoolSize);
    }

    /**
     * 这里的 await 机制只对 schedule 方法有效, 对另外的 scheduleAtFixedRate 和 scheduleWithFixedDelay 无效
     * @param command
     */
    @Override
    public ScheduledFuture<?> schedule(Runnable command,
                                       long delay,
                                       TimeUnit unit) {
        this.lock.lock();
        try {
            String maxBlockingQueueSize = System.getProperty(blockingQueueSizeLimitKey, DEFAULT_MAX_BLOCKING_QUEUE_SIZE);
            while (this.getPoolSize() >= this.getCorePoolSize() && this.getQueue().size() >= Integer.parseInt(maxBlockingQueueSize)) {
                this.condition.await(awaitTime, TimeUnit.SECONDS);
            }
            return super.schedule(command, delay, unit);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        this.lock.lock();
        try {
            this.condition.signalAll();
        } finally {
            this.lock.unlock();
        }
    }
}
