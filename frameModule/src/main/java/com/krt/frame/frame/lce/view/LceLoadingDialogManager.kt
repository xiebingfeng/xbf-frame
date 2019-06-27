package com.krt.frame.frame.lce.view

import android.widget.Toast
import com.krt.frame.BuildConfig
import com.krt.frame.R
import com.krt.frame.ext.showToast
import com.krt.frame.frame.dialog.LceLoadingDialog
import com.krt.frame.frame.lce.LceView
import com.krt.frame.frame.mvvm.LceError

class LceLoadingDialogManager : LceView {

    private var loadingDialog: LceLoadingDialog? = null

    fun getLoadingDialog(actionInitLoadingDialog: (() -> LceLoadingDialog)?): LceLoadingDialog? {
        if (null == loadingDialog) {
            loadingDialog = actionInitLoadingDialog?.invoke()
        }
        return loadingDialog
    }

    override fun showLoading(showDialog: Boolean) {
    }

    override fun showContent() {
        loadingDialog?.apply {
            this.dismissDialog()
        }
    }

    override fun showNoNetwork(showDialog: Boolean) {
        loadingDialog?.apply {
            this.dismissDialog()
        }

        showToast(R.string.frame_network_is_not_usable)
    }

    override fun showError(lceError: LceError) {
        loadingDialog?.apply {
            this.dismissDialog()
        }

        //如果不显示 错误界面，则做提示
        if (!lceError.errorContainerShow) {
            if (null == lceError.errorMsg) {
                showToast(R.string.frame_server_data_is_abnormal)
            } else {
                if (BuildConfig.DEBUG) {
                    showToast(lceError.errorMsg, Toast.LENGTH_LONG)
                } else {
                    showToast(lceError.errorMsg)
                }
            }
        }
    }

    fun dismiss() {
        loadingDialog?.dismissDialog()
    }

    fun isLoadingDialogNotNull(): Boolean {
        return loadingDialog?.isDialogNotNull() ?: false
    }

}