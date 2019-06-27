package com.krt.frame.ext

import com.orhanobut.logger.Logger

fun logV(text: String) {
    Logger.v(text)
}

fun logD(text: Any) {
    Logger.d(text)
}

fun logI(text: String) {
    Logger.i(text)
}

fun logE(text: String) {
    Logger.e(text)
}

fun logW(text: String) {
    Logger.w(text)
}