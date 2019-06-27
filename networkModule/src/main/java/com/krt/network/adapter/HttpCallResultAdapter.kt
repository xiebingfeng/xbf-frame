package com.krt.network.adapter

import com.alibaba.fastjson.JSONObject
import com.krt.frame.app.config.KRT
import com.orhanobut.logger.Logger
import java.lang.reflect.ParameterizedType

class HttpCallResultAdapter<T> {

    private var functionList: ((List<T>) -> Unit)? = null
    private var functionObject: ((T?) -> Unit)? = null

    private var transformByMainThread = true

    fun callBack(result: String, actionError: (String) -> Unit) {
        functionList?.apply {
            this.javaClass.declaredMethods.forEach { method ->
                if (method.genericParameterTypes.size == 1 && method.toString().contains("invoke(") && method.returnType.name == "void") {
                    val type = (method.genericParameterTypes[0] as ParameterizedType).actualTypeArguments[0] as Class<T>

                    if (transformByMainThread) {
                        val resultData: MutableList<T>?
                        try {
                            resultData = JSONObject.parseArray(result, type)
                        } catch (e: Exception) {
                            actionError.invoke(e.toString())
                            return@apply
                        }

                        KRT.getHandler()?.post {
                            this.invoke(resultData)
                        }
                    } else {
                        val resultData: MutableList<T>?
                        try {
                            resultData = JSONObject.parseArray(result, type)
                        } catch (e: Exception) {
                            actionError.invoke(e.toString())
                            return@apply
                        }
                        this.invoke(resultData)
                    }
                    Logger.json(result)
                    return
                }
            }
        }

        functionObject?.apply {
            this.javaClass.declaredMethods.forEach { method ->
                if (method.genericParameterTypes.size == 1 && method.toString().contains("invoke(") && method.returnType.name == "void") {
                    val type = method.genericParameterTypes[0] as Class<T>

                    if (transformByMainThread) {
                        val resultData: T?
                        try {
                            resultData = JSONObject.parseObject(result, type)
                        } catch (e: Exception) {
                            actionError.invoke(e.toString())
                            return@apply
                        }
                        KRT.getHandler()?.post {
                            this.invoke(resultData)
                        }
                    } else {
                        val resultData: T?
                        try {
                            resultData = JSONObject.parseObject(result, type)
                        } catch (e: Exception) {
                            actionError.invoke(e.toString())
                            return@apply
                        }
                        this.invoke(resultData)
                    }
                    Logger.json(result)
                    return
                }
            }
        }

    }

    fun toList(transformByMainThread: Boolean = true, function: (List<T>) -> Unit) {
        this.transformByMainThread = transformByMainThread
        this.functionList = function
    }

    fun toObject(transformByMainThread: Boolean = true, function: (T?) -> Unit) {
        this.transformByMainThread = transformByMainThread
        this.functionObject = function
    }
}