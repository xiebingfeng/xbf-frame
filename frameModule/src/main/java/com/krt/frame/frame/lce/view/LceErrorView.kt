package com.krt.frame.frame.lce.view

import android.text.method.ScrollingMovementMethod
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.TextView
import com.krt.frame.R
import com.krt.frame.ext.onClick
import com.krt.frame.ext.setVisible
import com.krt.frame.frame.mvvm.LceError

class LceErrorView(private val rootView: ViewGroup) {

    private var errorViewStub: ViewStub? = null

    private var errorView: View? = null

    fun getView(): View {
        if (errorView == null) {
            errorViewStub = rootView.findViewById(R.id.lce_error_view_stub)
            errorView = errorViewStub!!.inflate()
        }
        return errorView!!
    }

    fun initFunction(errorCallBack: () -> Unit, lceError: LceError) {
        getView().setOnClickListener {
            errorCallBack.invoke()
        }

        lceError.errorMsg?.let {
            errorView?.findViewById<TextView>(R.id.tv_error_show)?.apply {
                text = it
                movementMethod = ScrollingMovementMethod.getInstance()
                onClick {
                    errorCallBack.invoke()
                }
            }
        }
        getView().setVisible(View.VISIBLE)
    }

    fun setGoneIfNotNull() {
        errorView?.setVisible(View.GONE)
    }

}