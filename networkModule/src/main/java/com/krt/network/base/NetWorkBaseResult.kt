package com.krt.network.base

import com.alibaba.fastjson.annotation.JSONField
import com.krt.frame.fastjson.NotMix

@NotMix
class NetWorkBaseResult(
        @JSONField(name = "code")
        val code: Int?,
        @JSONField(name = "data")
        val `data`: String?,
        @JSONField(name = "msg")
        val msg: String?,
        @JSONField(name = "message")
        val message: String?
) {

    fun getCommonMessage(): String {
        return msg ?: message ?: ""
    }

}