package com.krt.frame.app

import android.content.Context
import android.os.Debug
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.service.DegradeService
import com.blankj.utilcode.util.ThreadUtils
import com.krt.frame.BuildConfig
import com.krt.frame.app.config.KRT
import com.krt.frame.ext.methodInvoke
import com.krt.frame.ext.showToast
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.concurrent.CountDownLatch

abstract class BaseApp : DegradeService {

    private var mustCallInterfaceBackThread = false

    private var appInitCountDownLatch: CountDownLatch? = null

    override fun init(context: Context?) {
    }

    override fun onLost(context: Context?, postcard: Postcard?) {
    }

    fun initModuleApp(): IModuleService {
        if (!ThreadUtils.isMainThread()) {
            initData()
        } else {
            KRT.getThreadPool().execute(Runnable {
                appInitCountDownLatch = CountDownLatch(1)
                val startTime = System.currentTimeMillis()
                initData()
                if (System.currentTimeMillis() - startTime > 60) {
                    mustCallInterfaceBackThread = true
                }
                appInitCountDownLatch!!.countDown()
            })
        }

        return getModuleInterfaceProxy()
    }

    private fun getModuleInterfaceProxy(): IModuleService {
        val serviceImpl = initModuleAppService()

        return Proxy.newProxyInstance(
                serviceImpl.javaClass.classLoader, serviceImpl.javaClass.interfaces, object : InvocationHandler {
            override fun invoke(proxy: Any, method: Method, args: Array<Any>?): Any? {
                val startTime = System.currentTimeMillis()
                appInitCountDownLatch?.let {
                    it.await()
                }

                val result = methodInvoke(serviceImpl, method, args)
                if (BuildConfig.DEBUG && ThreadUtils.isMainThread() && !Debug.isDebuggerConnected()) {
                    val useTime = System.currentTimeMillis() - startTime
                    if (useTime > 100) {
                        throw RuntimeException("you must use this interface in thread background")
                    } else if (useTime > 40) {
                        showToast("please use this interface in thread background")
                    }
                }
                return result
            }
        }
        ) as IModuleService
    }

    /**
     * 初始化ModuleService
     */
    abstract fun initModuleAppService(): IModuleService

    /**
     * 初始化数据，肯定是在异步线程上初始化的
     */
    abstract fun initData()
}
