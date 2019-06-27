package com.krt.network.adapter

import com.krt.network.base.NetWorkBaseResult

/**
 * Http请求结果适配器，默认是  {"code":200,"data":"","msg":""}格式，不同的项目可能请求结果格式不同，要进行转换
 *
 */
interface BaseHttpResultAdapter {

    fun adapter(text: String): NetWorkBaseResult

}