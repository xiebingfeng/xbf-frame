package com.krt.network.error

import com.krt.network.R
import com.krt.network.ext.NetWorkUtils
import com.lzy.okgo.exception.HttpException
import java.net.SocketTimeoutException
import javax.net.ssl.SSLHandshakeException

object NetWorkError {

    val NET_WORK_ERROR_NO_NET_WORK = NetWorkUtils.getString(R.string.frame_network_no_net)


    fun convertStatusCode(exception: Throwable): String =
            if (exception is HttpException) {
                when {
                    exception.code() in 500..599 -> "服务器发生错误"
                    exception.code() == 404 -> "请求地址不存在"
                    exception.code() == 403 -> "请求被服务器拒绝"
                    exception.code() == 400 -> "客户端请求语法错误"
                    exception.code() == 307 -> "请求被重定向其他页面"
                    exception.code() == 203 -> "返回信息不确定或不完整"
                    exception.code() == 204 -> "请求收到，但返回信息为空"
                    else -> NetWorkUtils.getString(R.string.frame_network_load_error)
                }
            } else if (exception is SocketTimeoutException) {
                "网络加载超时"
            } else if (exception is SSLHandshakeException) {
                "证书异常"
            } else {
                NetWorkUtils.getString(R.string.frame_network_load_error)
            }

}