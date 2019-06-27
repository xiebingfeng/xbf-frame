package com.krt.frame.app.config

import android.app.Activity
import android.os.Handler
import com.krt.frame.app.config.net.NetworkCustomMade
import com.krt.frame.app.config.net.NetworkDefaultHeaders
import com.krt.frame.frame.dialog.LceLoadingDialog
import okhttp3.Interceptor
import java.io.File
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList

class Configurator {

    init {
        KRT_CONFIGS[ConfigKeys.CONFIG_READY] = false
        KRT_CONFIGS[ConfigKeys.HANDLER] =
                HANDLER
    }

    /**
     * 获取数据储存的集合
     *
     * @return
     */
    fun getConfigs(): HashMap<Any, Any?> {
        return KRT_CONFIGS
    }

    /**
     * 根据枚举获取内容
     *
     * @param key
     * @param <T>
     * @return
    </T> */
    fun <T> getConfiguration(key: Any): T {
        checkConfiguration()
        return KRT_CONFIGS[key] as T
    }

    /**
     * 初始化构建完成
     */
    fun configure() {
        KRT_CONFIGS[ConfigKeys.CONFIG_READY] = true
    }

    /**
     * 设置网络请求的地址 host
     *
     * @param host
     * @return
     */
    fun withHost(host: String): Configurator {
        KRT_CONFIGS[ConfigKeys.API_HOST] = host
        return this
    }

    /**
     * 设置 基础文件目录
     */
    fun withBaseFilePath(baseFilePath: String): Configurator {
        KRT_CONFIGS[ConfigKeys.BASE_FILE_PATH] = baseFilePath
        return this
    }

    fun withCacheFilePath(cacheFilePath: String): Configurator {
        KRT_CONFIGS[ConfigKeys.CACHE_FILE_PATH] = cacheFilePath

        HANDLER.post {
            if (!File(cacheFilePath).exists()) {
                File(cacheFilePath).mkdirs()
            }
        }
        return this
    }

    fun withGlideCacheFilePath(cacheFilePath: String): Configurator {
        KRT_CONFIGS[ConfigKeys.GLIDE_CACHE_FILE_PATH] = cacheFilePath
        return this
    }

    /**
     * 添加网络默认请求头
     */
    fun withDefaultHttpHeaders(headers: NetworkDefaultHeaders): Configurator {
        KRT_CONFIGS[ConfigKeys.DEF_HTTP_HEADERS] = headers
        return this
    }

    fun withFragmentSkipAnimTime(time: Long): Configurator {
        KRT_CONFIGS[ConfigKeys.FRAGMENT_SKIP_ANIM_TIME] = time
        return this
    }

    fun withDefaultLoadingDialog(loadingDialog: LceLoadingDialog): Configurator {
        KRT_CONFIGS[ConfigKeys.LOADING_DIALOG] = loadingDialog
        return this
    }

    /**
     * 设置 JavaScript 接口名称
     */
    fun withJavascriptInterface(name: String): Configurator {
        KRT_CONFIGS[ConfigKeys.JAVASCRIPT_INTERFACE] = name
        return this
    }

    /**
     * 设置当前Activity
     */
    fun withActivity(activity: Activity?): Configurator {
        activity?.let {
            KRT_CONFIGS.put(ConfigKeys.CURRENT_ACTIVITY, WeakReference<Activity>(activity))
        }
        return this
    }

    /**
     * 检查是否初始化完毕
     */
    private fun checkConfiguration() {
        val isReady = KRT_CONFIGS[ConfigKeys.CONFIG_READY] as Boolean
        if (!isReady) {
            throw RuntimeException("configuration is not ready")
        }
    }

    fun withNetworkCustomMade(customMadeList: ArrayList<NetworkCustomMade>): Configurator {
        KRT_CONFIGS[ConfigKeys.NETWORK_CUSTOM_MADE] = customMadeList
        return this
    }

    /**
     *  Http请求结果适配器
     *
     * @param baseHttpResultAdapter  必须是BaseHttpResultAdapter，否则会出错
     * @return
     */
    fun withNetworkResultAdapter(baseHttpResultAdapter: Any): Configurator {
        KRT_CONFIGS[ConfigKeys.NETWORK_RESULT_ADAPTER] = baseHttpResultAdapter
        return this
    }

    companion object {

        fun getInstance(): Configurator {
            return Holder.INSTANCE
        }

        // 各类数据的Map储存
        private var KRT_CONFIGS = HashMap<Any, Any?>()
        // 拦截器集合
        private val INTERCEPTORS = ArrayList<Interceptor>()

        private var HANDLER = Handler()
    }

    private object Holder {
        val INSTANCE = Configurator()
    }
}