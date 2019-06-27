package com.krt.frame.app

import android.app.Activity
import android.content.Context
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by xbf on 2018/4/5.
 */

object AppManager {
    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity) {
        val reference = WeakReference(activity)
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack?.add(reference.get())
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity? {
        return if (activityStack?.size!! > 0)
            activityStack?.lastElement()
        else
            null
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishCurrentActivity() {
        activityStack?.lastElement().let {
            finishActivity(it)
        }
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        activity.let {
            activityStack?.remove(activity)
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        for (activity in activityStack!!) {
            if (activity.javaClass == cls) {
                finishActivity(activity)
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        var i = 0
        val size = activityStack?.size ?: 0
        while (i < size) {
            if (null != activityStack!![i]) {
                activityStack!![i].finish()
            }
            i++
        }
        activityStack?.clear()
    }

    /**
     * 退出应用程序
     */
    fun exitApp(context: Context) {
        try {
            finishAllActivity()
            System.exit(0)
            android.os.Process.killProcess(android.os.Process.myPid())
        } catch (e: Exception) {
        }

    }

    /**
     * 是否存在一个ACTIVITY
     */
    fun containActivity(className: String): Boolean {
        activityStack?.forEach {
            if (it.localClassName == className) {
                return true
            }
        }
        return false
    }

    private var activityStack: Stack<Activity>? = null
}
