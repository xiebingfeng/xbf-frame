package com.krt.frame.ext

import android.widget.Toast
import com.blankj.utilcode.util.ThreadUtils
import com.blankj.utilcode.util.ToastUtils
import com.krt.frame.app.config.KRT

fun showToast(messageId: Int) {
    showToast(KRT.getApplicationContext().resources.getString(messageId))
}


fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    if (ThreadUtils.isMainThread()) {
        showCustomToast(message, duration)
    } else {
        KRT.getHandler()?.post {
            showCustomToast(message, duration)
        }
    }
}

private fun showCustomToast(message: String, duration: Int) {
    if (duration == Toast.LENGTH_LONG) {
        ToastUtils.showLong(message)
    } else {
        ToastUtils.showShort(message)
    }
}
