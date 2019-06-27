package com.krt.network

import com.alibaba.fastjson.JSONObject
import com.blankj.utilcode.util.NetworkUtils
import com.krt.frame.app.config.ConfigKeys
import com.krt.frame.app.config.KRT
import com.krt.frame.app.config.net.NetworkCustomMade
import com.krt.frame.app.config.net.NetworkDefaultHeaders
import com.krt.network.adapter.BaseHttpResultAdapter
import com.krt.network.adapter.HttpCallResultAdapter
import com.krt.network.base.LCEParams
import com.krt.network.base.NetWorkBaseResult
import com.krt.network.base.UpFileParams
import com.krt.network.error.NetWorkError
import com.krt.network.ext.NetWorkUtils
import com.krt.network.request.CommonRequest
import com.lzy.okgo.model.HttpHeaders
import com.lzy.okgo.model.HttpParams
import com.lzy.okgo.model.Response
import com.lzy.okgo.request.base.Request
import com.orhanobut.logger.Logger
import com.tencent.bugly.crashreport.CrashReport
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

fun <T> httpCall(
        url: String,   //url 地址
        getRequestMode: Boolean,  //true:GET   false:POST
        tag: Any?,     //版定 OKGO生命对象的控件,常为ViewModel或View

        //MORE : PARAMS
        upJson: String? = null,         //上传的字符串
        httpParams: HttpParams? = null,   //参数
        upFile: UpFileParams? = null,

        //MORE : HEAD
        defaultTokenHead: Boolean = false,  //是否加载默认头
        headers: HttpHeaders? = null,    //头字段

        //MORE : LCE MODE
        lce: LCEParams = LCEParams(),

        //MORE : CALLBACK
        actionCallBackError: ((String) -> Unit)? = null,
        callBackSuccess: ((String) -> Unit)? = null     //请未数据成功后，返回获取结果，不会任何处理
): HttpCallResultAdapter<T> {
    Logger.w("url,url,url,url,url,url,url,url,url,url:          $url")

    val returnBack = HttpCallResultAdapter<T>()

    //1.设置TAG，主要是解决网络请求的生命周期问题
    val httpTagManager = HttpTagManager(tag)

    //2.先判断当前网络是否可用
    if (!NetworkUtils.isConnected()) {
        if (!lce.notStartLce) {
            httpTagManager.showNoNetWork(lce.showLoadingDialog)
        }
        actionCallBackError?.invoke(NetWorkError.NET_WORK_ERROR_NO_NET_WORK)
        return returnBack
    }

    //3.设置  Request
    val commonRequest = CommonRequest.Factory().create(getRequestMode)

    //4.设置请求并配置一些参数
    val request = commonRequest.createHttpRequest(getRequestMode, url, tag)
    setRequestProperty(
            request,
            commonRequest,
            defaultTokenHead,
            upJson,
            upFile,
            headers,
            httpParams
    )

    //5.错误处理
    fun onError(errorMsg: String) {
        Logger.e("onError:            $errorMsg")

        KRT.getHandler()?.post {
            actionCallBackError?.invoke(errorMsg)
            if (!lce.notStartLce) {
                httpTagManager.showError(errorMsg, lce.isErrorContainerShow)
            }
        }
    }

    //6.正确处理
    fun onSuccess(result: String) {
        run outside@{
            var isParseJsonError = false
            //先转载数据 再显示内容，这样效果会好点。  不过这样的话，列表数据为空时，有点小坑
            if (null != callBackSuccess) {
                KRT.getHandler()?.post {
                    callBackSuccess.invoke(result)
                }
            } else {
                returnBack.callBack(result) {
                    onError(it)
                    isParseJsonError = true
                }
            }

            if (!lce.notStartLce && !lce.notShowContentWhenSuccess && !isParseJsonError) {
                KRT.getHandler()?.post {
                    httpTagManager.showContent()
                }
            }
        }
    }

    //7.开始请求
    commonRequest.adapt(request)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                //加载动画
                if (!lce.notStartLce) {
                    httpTagManager.showLoading(lce.showLoadingDialog)
                }
            }
            .observeOn(Schedulers.io())
            .subscribe(object : Observer<Response<String>> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(response: Response<String>) {
                    val result = response.body()

                    Logger.w("onNext,onNext,onNext,onNext:          $result")

                    if (null != callBackSuccess) {
                        onSuccess(response.body())
                        return
                    }

                    var resultBean: NetWorkBaseResult?
                    try {
                        resultBean = JSONObject.parseObject(result, NetWorkBaseResult::class.java)
                        if (resultBean?.code == null) {
                            KRT.getConfiguration<BaseHttpResultAdapter>(ConfigKeys.NETWORK_RESULT_ADAPTER)?.apply {
                                resultBean = adapter(result)
                            }

                        }
                    } catch (e: Exception) {
                        onError(e)
                        return
                    }

                    if (resultBean?.code == 200) {
                        onSuccess(resultBean?.data ?: "")
                        return
                    } else {
                        /**
                         * Code 拦截
                         */
                        KRT.getConfiguration<List<NetworkCustomMade>>(ConfigKeys.NETWORK_CUSTOM_MADE)?.forEach {
                            if (it.code == resultBean?.code && it.action.invoke()) {
                                onError(it.warn)
                                return
                            }
                        }

                        if (null != lce.errorWarn) {
                            onError(lce.errorWarn)
                        } else {
                            if (resultBean?.getCommonMessage()?.isNotEmpty() == true) {
                                onError(resultBean!!.getCommonMessage())
                            } else {
                                onError(NetWorkUtils.getString(R.string.frame_network_error))
                            }
                        }
                    }
                }

                override fun onError(e: Throwable) {
                    CrashReport.postCatchedException(e)
                    onError(NetWorkError.convertStatusCode(e))
                }

                override fun onComplete() {
                }
            })
    return returnBack
}

private fun setRequestProperty(
        request: Request<*, *>,
        requestStrategy: CommonRequest,
        defaultTokenHead: Boolean,
        upJson: String?,
        upFileParams: UpFileParams?,
        headers: HttpHeaders?,
        httpParams: HttpParams?
) {
    //上传的JSON串，会以JSON的格式上传，主要是mediaType
    requestStrategy.upJson(request, upJson)
    upJson?.let {
        Logger.w("upJson,upJson,upJson,upJson:       $it")
        Logger.json(it)
    }

    //上传的文件
    requestStrategy.addFileParams(request, upFileParams)

    httpParams?.let {
        request.params(it)
        Logger.w("httpParams,httpParams,httpParams:     $httpParams")
    }

    if (defaultTokenHead) {
        (KRT.getConfiguration(ConfigKeys.DEF_HTTP_HEADERS) as? NetworkDefaultHeaders)?.let {
            request.headers(it.action.invoke())
        }

        Logger.d("defaultTokenHead:    $defaultTokenHead")
    }

    headers?.let {
        request.headers(it)
        Logger.d("headers:     $headers")
    }
}
