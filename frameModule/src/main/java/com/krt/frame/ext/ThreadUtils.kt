package com.krt.frame.ext

import android.os.Looper
import com.krt.frame.app.config.KRT

/**
 * 子线程转主线程
 */
fun <R> (() -> R).backToMain(function: ((R) -> Unit)? = null) {
    KRT.getThreadPool().execute(Runnable {
        val result = this.invoke()
        KRT.getHandler()?.post {
            function?.invoke(result)
        }
    })
}

/**
 * 主线程空闲时再执行
 */
fun executeWhenThreadIsIdle(action: () -> Unit) {
    Looper.myQueue().addIdleHandler {
        action.invoke()
        false
    }
}
