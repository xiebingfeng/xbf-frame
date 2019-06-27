package com.krt.frame.cache

import com.blankj.utilcode.util.ThreadUtils
import com.krt.frame.app.config.KRT
import java.util.concurrent.CountDownLatch

abstract class BaseStaticCache<T> {

    private var entity: T? = null

    /**
     * 保存数据，1.保存到内存中 2.保存到本地
     *
     * @param entity
     */
    fun saveData(entity: T) {
        this.entity = entity
        clearDataThenSaveByDisk(entity)
    }

    /**
     * 判断实体类是否存在
     * 内存中不存在的话，从数据库中读取，并缓存到内存中
     */
    fun getData(): T? {
        entity?.let { return it }

        var checkKey: CountDownLatch? = null

        if (ThreadUtils.isMainThread()) {
            checkKey = CountDownLatch(1)
        }

        if (null != checkKey) {
            checkKey.let {
                KRT.getThreadPool().execute(Runnable {
                    entity = loadDataFromDisk()
                    checkKey.countDown()
                })
                it.await()
            }
        } else {
            entity = loadDataFromDisk()
        }
        entity?.let {
            return it
        }
        return null
    }

    fun clearData() {
        entity = null
    }

    fun isExit(): Boolean {
        return null != entity
    }

    /**
     * 保存数据到硬盘中，SP或数据库
     */
    abstract fun loadDataFromDisk(): T?

    /**
     * 先清除再保存
     */
    abstract fun clearDataThenSaveByDisk(entity: T)

}