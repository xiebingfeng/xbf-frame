package com.krt.frame.frame.fragment

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.krt.frame.frame.mvvm.BaseViewModel
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

abstract class BaseVMFragment<VM : BaseViewModel> : BaseFragment() {

    var viewModel: VM? = null

    open fun initViewModelLiveData() {}

    open fun initViewModelLiveDataAfterAnimationEnd() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createViewModel().let {
            viewModel = it
            initViewModelLiveData()
        }
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun createViewModel(): VM {
        sequence<Type> {
            var thisClass: Class<*> = this@BaseVMFragment.javaClass
            while (true) {
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

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)

        /**
         * 如果当前Fragment有父容器的话，父容器还在动画中，可当前Fragment动画已经结束了，这时候加载数据就会引起动画的卡顿
         */
        val parentFragment = getParentFragmentByThis()
        if (parentFragment != null) {
            if (parentFragment.isAnimationEnd) {
                initViewModelLiveDataAfterAnimationEnd()
            } else {
                parentFragment.showChildFragmentEnterAnimationEnd = true
            }
        } else {
            initViewModelLiveDataAfterAnimationEnd()
        }
    }
}
