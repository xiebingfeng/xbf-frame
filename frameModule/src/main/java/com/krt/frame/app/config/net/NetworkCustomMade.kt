package com.krt.frame.app.config.net

class NetworkCustomMade(
        val code: Int,
        val warn: String,
        var action: () -> Boolean
)