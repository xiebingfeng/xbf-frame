package com.krt.network.error;

import android.content.Context;

/**
 * @author xiebingfeng
 */
public interface IResponseErrorListener {

    void handleResponseError(Context context, Exception e);

}
