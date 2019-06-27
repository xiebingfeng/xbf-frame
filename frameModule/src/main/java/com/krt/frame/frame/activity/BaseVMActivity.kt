package com.krt.frame.frame.activity

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.krt.frame.frame.mvvm.BaseViewModel
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

abstract class BaseVMActivity<VM : BaseViewModel> : BaseActivity() {

    var viewModel: VM? = null

    protected abstract fun initViewModelLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createViewModel().let {
            viewModel = it
            initViewModelLiveData()
        }
    }

    private fun createViewModel(): VM {
        sequence<Type> {
            var thisClass: Class<*> = this@BaseVMActivity.javaClass
            while (true) {
                @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                yield(thisClass.genericSuperclass)
                thisClass = thisClass.superclass ?: break
            }
        }.filter {
            it is ParameterizedType
        }.flatMap {
            (it as ParameterizedType).actualTypeArguments.asSequence()
        }.first {
            it is Class<*> && BaseViewModel::class.java.isAssignableFrom(it)
        }.let {
            return ViewModelProviders.of(this).get(it as Class<VM>)
        }
    }

}