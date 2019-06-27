package com.krt.network

import com.krt.network.adapter.HttpCallResultAdapter
import com.krt.network.base.LCEParams
import com.krt.network.base.UpFileParams
import com.lzy.okgo.model.HttpHeaders
import com.lzy.okgo.model.HttpParams

fun <T> httpPost(
        url: String,
        tag: Any?,

        upJson: String? = null,
        upFile: UpFileParams? = null,
        httpParams: HttpParams? = null,

        defaultTokenHead: Boolean = false,
        headers: HttpHeaders? = null,

        //MORE : LCE MODE
        lce: LCEParams = LCEParams(),

        //MORE : CALLBACK
        actionCallBackError: ((String) -> Unit)? = null,
        callBackSuccess: ((String) -> Unit)? = null
): HttpCallResultAdapter<T> {
    return httpCall(
            url,
            false,
            tag,
            upJson = upJson,
            upFile = upFile,
            httpParams = httpParams,
            defaultTokenHead = defaultTokenHead,
            headers = headers,
            lce = lce,
            actionCallBackError = actionCallBackError,
            callBackSuccess = callBackSuccess
    )
}

fun <T> httpGet(
        url: String,
        tag: Any?,

        //MORE : PARAMS
        httpParams: HttpParams? = null,

        //MORE : HEAD
        defaultTokenHead: Boolean = false,
        headers: HttpHeaders? = null,

        //MORE : LCE MODE
        lce: LCEParams = LCEParams(),

        //MORE : CALLBACK
        actionCallBackError: ((String) -> Unit)? = null,
        callBackSuccess: ((String) -> Unit)? = null
): HttpCallResultAdapter<T> {
    return httpCall(
            url,
            true,
            tag,
            httpParams = httpParams,
            defaultTokenHead = defaultTokenHead,
            headers = headers,
            lce = lce,
            actionCallBackError = actionCallBackError,
            callBackSuccess = callBackSuccess
    )
}
