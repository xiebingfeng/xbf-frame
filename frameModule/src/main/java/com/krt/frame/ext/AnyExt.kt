package com.krt.frame.ext

fun Any?.toArray(): List<Any>? {
    if (this == null) {
        return null
    }
    val list = ArrayList<Any>()
    list.add(this)
    return list
}