package com.krt.frame.app

import java.util.concurrent.*


class ThreadPoolManager private constructor() {

    private val executor: ExecutorService

    private object Holder {
        val INSTANCE = ThreadPoolManager()
    }

    init {
        // 参数初始化
        val CPU_COUNT = Runtime.getRuntime().availableProcessors()
        // 核心线程数量大小
        val corePoolSize = Math.max(2, Math.min(CPU_COUNT - 1, 4))
        // 线程池最大容纳线程数
        val maximumPoolSize = CPU_COUNT * 2 + 1
        // 线程空闲后的存活时长
        val keepAliveTime = 30
        // keepAliveTime的单位参数
        val unit = TimeUnit.SECONDS

        executor = ThreadPoolExecutor(
                corePoolSize, //当某个核心任务执行完毕，会依次从缓冲队列中取出等待任务
                maximumPoolSize, //5,先corePoolSize,然后new LinkedBlockingQueue<Runnable>(),然后maximumPoolSize,但是它的数量是包含了corePoolSize的
                keepAliveTime.toLong(), //表示的是maximumPoolSize当中等待任务的存活时间
                unit, // keepAliveTime的单位参数
                LinkedBlockingQueue(128), //缓冲队列，用于存放等待任务，Linked的先进先出
                Executors.defaultThreadFactory(), //创建线程的工厂
                ThreadPoolExecutor.AbortPolicy()                //用来对超出maximumPoolSize的任务的处理策略
        )
    }

    /**
     * 执行任务
     */
    fun execute(runnable: Runnable) {
        executor.execute(runnable)
    }

    /**
     * 执行任务,相对耗时，如果不要求返回接货的话
     */
    fun submit(runnable: Runnable): Future<*> {
        return executor.submit(runnable)
    }

    companion object {

        val instance: ThreadPoolManager
            get() = ThreadPoolManager.Holder.INSTANCE
    }
}