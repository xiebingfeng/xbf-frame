package com.krt.network

import com.krt.frame.frame.lce.LceView
import com.krt.frame.frame.mvvm.LceError

class HttpTagManager(val tag: Any?) {

    fun showNoNetWork(showLoadingDialog: Boolean) {
        if (tag is LceView) {
            tag.showNoNetwork(showLoadingDialog)
        }
    }

    fun showError(errorMsg: String, isErrorContainerShow: Boolean) {
        if (tag is LceView) {
            tag.showError(LceError(errorMsg, isErrorContainerShow))
        }
    }

    fun showContent() {
        if (tag is LceView) {
            tag.showContent()
        }
    }

    fun showLoading(showLoadingDialog: Boolean) {
        if (tag is LceView) {
            tag.showLoading(showLoadingDialog)
        }
    }
}