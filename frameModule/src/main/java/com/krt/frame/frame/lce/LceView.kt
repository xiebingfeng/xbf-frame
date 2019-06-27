package com.krt.frame.frame.lce

import com.krt.frame.frame.mvvm.LceError


interface LceView {

    /**
     * 是否显示加载页面(是否是下拉刷新组件)
     */
    fun showLoading(showDialog: Boolean)

    /**
     * 显示内容页面
     */
    fun showContent()

    /**
     * 显示错误页面
     */
    fun showError(lceError: LceError)

    /**
     * 显示无网络页面
     */
    fun showNoNetwork(showDialog: Boolean)

}
