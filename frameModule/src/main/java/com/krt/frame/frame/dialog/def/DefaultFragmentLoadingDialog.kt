package com.krt.frame.frame.dialog.def

import android.support.v4.app.FragmentManager
import android.support.v4.app.SupportActivity
import com.krt.frame.frame.dialog.LceLoadingDialog
import com.krt.frame.frame.dialog.LoadingDialogFragment

internal class DefaultFragmentLoadingDialog : LceLoadingDialog {

    private var loadingDialog: LoadingDialogFragment? = null

    override fun showDialog(fragmentManager: FragmentManager?, activity: SupportActivity?, dialogCancelCallBack: () -> Unit) {
        if (null != loadingDialog) {
            return
        }

        if (null == loadingDialog) {
            loadingDialog = LoadingDialogFragment()

            loadingDialog?.apply {
                setOnDismissListener {
                    loadingDialog = null
                }
                setOnCancelListener {
                    dialogCancelCallBack.invoke()
                }
                this.show(fragmentManager, "LoadingDialogFragment")
            }
        }
    }

    override fun isDialogNotNull(): Boolean {
        return null != loadingDialog
    }

    override fun dismissDialog() {
        loadingDialog?.dismiss()
    }

}