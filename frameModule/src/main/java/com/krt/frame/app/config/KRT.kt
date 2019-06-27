package com.krt.frame.app.config

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import com.alibaba.android.arouter.launcher.ARouter
import com.krt.frame.BuildConfig
import com.krt.frame.app.AppManager
import com.krt.frame.app.ThreadPoolManager
import com.krt.frame.app.crash.CrashHandler
import com.krt.frame.frame.activity.BaseActivity
import com.krt.frame.frame.fragment.BaseFragment
import com.lzy.okgo.OkGo
import com.lzy.okgo.cache.CacheMode
import com.lzy.okgo.cookie.CookieJarImpl
import com.lzy.okgo.cookie.store.DBCookieStore
import com.lzy.okgo.interceptor.HttpLoggingInterceptor
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.DiskLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import com.tencent.bugly.crashreport.CrashReport
import okhttp3.OkHttpClient
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit
import java.util.logging.Level

/**
 * Created by xbf on 2018/4/5.
 */
@SuppressLint("StaticFieldLeak")
object KRT {

    private const val NET_TIME_OUT = 15000.toLong()
    private const val NET_TIME_OUT_DEBUG = 4000.toLong()

    private lateinit var application: Context

    var logEnable = true

    /**
     * 初始化 并保存applicationContext
     *
     * @param context applicationContext
     * @return
     */
    fun init(context: Context, initLeakCanary: Boolean, actionThreadInit: (() -> Unit)? = null): Configurator {
        application = context
        Thread(Runnable {
            if (logEnable) {
                initLog()
            }
            ARouter.init(context as Application)
            //初始化业务模块
            actionThreadInit?.invoke()
            initOkGo(context)
            if (initLeakCanary) {
//                initLeakCanary(context)
//                BlockCanary.install(context, BlockCanaryContext(1000)).start()
            }
            CrashHandler.init(context) //必须放在bugly前，否则会出问题
            initBugly(context)
//            ZXingLibrary.initDisplayOpinion(context)
        }).start()

        return getConfigurators()
    }

    /**
     * 获取数据储存的集合
     *
     * @return
     */
    fun getConfigurators() = Configurator.getInstance()

    /**
     * 获取各类数据
     *
     * @param key
     * @param <T>
     * @return
    </T> */
    fun <T> getConfiguration(key: ConfigKeys) = (getConfigurators().getConfiguration(key)) as? T

    /**
     * 获取handler对象
     *
     * @return
     */
    fun getHandler() = getConfiguration(ConfigKeys.HANDLER) as? Handler

    fun getApplicationContext() = application

    /**
     * 获取当前最前面的Activity
     */
    fun getCurrentActivity(): Activity? {
        var activity = (getConfigurators().getConfiguration(ConfigKeys.CURRENT_ACTIVITY) as? WeakReference<Activity>)?.get()
        if (activity == null) {
            activity = AppManager.currentActivity()
        }
        return activity
    }

    fun getCurrentFragment(): BaseFragment? {
        getCurrentActivity()?.apply {
            return if (this is BaseActivity) {
                this.topFragment as BaseFragment
            } else {
                null
            }
        }
        return null
    }

    /**
     * Fragment的跳转动画时间
     */
    fun getFragmentAnimSkipTime() = getConfigurators().getConfiguration<Long>(ConfigKeys.FRAGMENT_SKIP_ANIM_TIME)

    /**
     * 获取线程池
     *
     * @return
     */
    fun getThreadPool() = ThreadPoolManager.instance

    /**
     * 初始化Log
     */
    private fun initLog() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
                // (Optional) Whether to show thread info or not. Default true
                .showThreadInfo(BuildConfig.DEBUG)
                // (Optional) How many method line to show. Default 2
//                .methodCount(0)
                // (Optional) Skips some method invokes in stack trace. Default 5
//                .methodOffset(5)
                // (Optional) Custom tag for each log. Default PRETTY_LOGGER
                .tag("KRT_")
                .build()

        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))

        //目前是打印到   根文件  logger目录下
        Logger.addLogAdapter(DiskLogAdapter())
    }

    /**
     * 初始化okgo
     *
     * @param context
     */
    private fun initOkGo(context: Context) {
        val builder = OkHttpClient.Builder()
        //log相关
        val loggingInterceptor = HttpLoggingInterceptor("OkGo")
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY)        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO)                               //log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor)                                 //添加OkGo默认debug日志

        val time = if (BuildConfig.DEBUG) NET_TIME_OUT_DEBUG else NET_TIME_OUT

        builder.readTimeout(time, TimeUnit.MILLISECONDS)      //全局的读取超时时间
        builder.writeTimeout(time, TimeUnit.MILLISECONDS)     //全局的写入超时时间
        builder.connectTimeout(time, TimeUnit.MILLISECONDS)   //全局的连接超时时间

        //自动管理cookie（或者叫session的保持），以下几种任选其一就行
        //        //builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));            //使用sp保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(CookieJarImpl(DBCookieStore(context)))              //使用数据库保持cookie，如果cookie不过期，则一直有效
        OkGo.getInstance().init(context as Application)                           //必须调用初始化
                .setCacheMode(CacheMode.NO_CACHE)
                .setOkHttpClient(builder.build()).retryCount = 1                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0

        OkGo.getInstance().okHttpClient.connectTimeoutMillis()
    }

    internal var refWatcher: RefWatcher? = null

    private fun initLeakCanary(context: Context) {
        if (LeakCanary.isInAnalyzerProcess(context)) {
            return
        }
        refWatcher = LeakCanary.install(context as Application)
    }

    private fun initBugly(context: Context) {
        context.apply {
            val applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)

            applicationInfo.metaData?.getString("BUGLY_APPID").let {
                CrashReport.initCrashReport(applicationContext, it, false)
            }
        }
    }

}