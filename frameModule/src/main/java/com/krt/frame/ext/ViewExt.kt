package com.krt.frame.ext

import android.view.View
import com.blankj.utilcode.util.KeyboardUtils

/**
 * 谨慎 用View.GONE  会让整体容器重新绘画
 */
fun View.setVisible(it: Int) {
    if (this.visibility != it) {
        this.visibility = it
    }
}

fun View.onClick(hideSoft: Boolean? = true, l: (v: android.view.View?) -> Unit) {
    triggerDelay = 400
    setOnClickListener {
        if (clickEnable()) {
            if (hideSoft!!) {
                KeyboardUtils.hideSoftInput(this)
            }
            l.invoke(this)
        }
    }
}

private fun <T : View> T.clickEnable(): Boolean {
    var flag = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= triggerDelay) {
        flag = true
    }
    triggerLastTime = currentClickTime
    return flag
}

private var <T : View> T.triggerLastTime: Long
    get() = if (getTag(1123460103) != null) getTag(1123460103) as Long else 0
    set(value) {
        setTag(1123460103, value)
    }

private var <T : View> T.triggerDelay: Long
    get() = if (getTag(1123461123) != null) getTag(1123461123) as Long else -1
    set(value) {
        setTag(1123461123, value)
    }