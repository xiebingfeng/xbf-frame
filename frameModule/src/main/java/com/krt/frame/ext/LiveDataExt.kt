package com.krt.frame.ext

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.support.annotation.NonNull


fun <T> MutableLiveData<T>.obs(@NonNull owner: LifecycleOwner, action: (T) -> Unit) {
    this.observe(owner, Observer {
        it?.let {
            action.invoke(it)
        }
    })
}