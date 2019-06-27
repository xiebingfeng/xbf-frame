package com.krt.frame.frame.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.krt.frame.R


/**
 * Created by xbf on 2018/2/8.
 */

class LoadingDialogFragment : DialogFragment() {

    private var functionDismiss: (() -> Unit)? = null

    private var functionCancel: (() -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.setCanceledOnTouchOutside(false)

        val window = this.dialog.window
        val lp = window?.attributes
        lp?.windowAnimations = R.style.frame_loading_animation
        window?.attributes = lp

        return inflater.inflate(R.layout.frame_dialog_loading, null)
    }


    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)
        functionCancel?.invoke()
    }

    override fun onDestroy() {
        super.onDestroy()
        functionDismiss?.invoke()
    }

    fun setOnDismissListener(function: () -> Unit) {
        functionDismiss = function
    }

    fun setOnCancelListener(function: () -> Unit) {
        functionCancel = function
    }
}

