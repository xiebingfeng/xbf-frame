package com.krt.frame.frame.dialog.def

import android.support.v4.app.FragmentManager
import android.support.v4.app.SupportActivity
import com.krt.frame.frame.dialog.LceLoadingDialog
import com.krt.frame.frame.dialog.LoadingDialog

/**
 * 普通Dialog
 */
internal class DefaultLoadingDialog : LceLoadingDialog {

    private var loadingDialog: LoadingDialog? = null

    override fun showDialog(fragmentManager: FragmentManager?, activity: SupportActivity?, dialogCancelCallBack: () -> Unit) {
        if (null == activity || null != loadingDialog) {
            return
        }
        loadingDialog = LoadingDialog(activity)
        loadingDialog?.apply {
            setOnDismissListener {
                loadingDialog = null
            }
            setOnCancelListener {
                dialogCancelCallBack.invoke()
            }
            show()
        }
    }

    override fun isDialogNotNull(): Boolean {
        return null != loadingDialog
    }

    override fun dismissDialog() {
        loadingDialog?.dismiss()
    }

}