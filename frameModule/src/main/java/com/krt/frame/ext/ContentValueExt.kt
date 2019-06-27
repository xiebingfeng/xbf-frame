package com.krt.frame.ext

import android.content.ContentValues

fun ContentValues.putAny(s: String, value: Any) {
    when (value) {
        is Byte -> this.put(s, value)
        is Short -> this.put(s, value)
        is ByteArray -> this.put(s, value)
        is String -> this.put(s, value)
        is Float -> this.put(s, value)
        is Int -> this.put(s, value)
        is Long -> this.put(s, value)
        is Double -> this.put(s, value)
        is Boolean -> this.put(s, value)
    }
}