package com.krt.frame.frame.dialog

import android.support.v4.app.FragmentManager
import android.support.v4.app.SupportActivity
import com.krt.frame.frame.dialog.def.DefaultFragmentLoadingDialog
import com.krt.frame.frame.dialog.def.DefaultLoadingDialog

interface LceLoadingDialog {

    fun showDialog(fragmentManager: FragmentManager?, activity: SupportActivity?, dialogCancelCallBack: () -> Unit)

    fun isDialogNotNull(): Boolean

    fun dismissDialog()

    class Factory {

        fun create(isFragmentDialog: Boolean? = true): LceLoadingDialog {
            return if (isFragmentDialog!!) {
                DefaultFragmentLoadingDialog()
            } else {
                DefaultLoadingDialog()
            }
        }

    }
}