package com.krt.frame.frame.lce.view

import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import com.krt.frame.R
import com.krt.frame.ext.setVisible

class LceNetworkView(private val rootView: ViewGroup) {

    private var noNetwrokViewStub: ViewStub? = null

    private var noNetwrokView: View? = null

    fun getView(): View {
        if (noNetwrokView == null) {
            noNetwrokViewStub = rootView.findViewById(R.id.lce_no_netwrok_view_stub)
            noNetwrokView = noNetwrokViewStub!!.inflate()
        }
        return noNetwrokView!!
    }

    fun initFunction(errorCallBack: () -> Unit) {
        getView().findViewById<View>(R.id.no_net_work).setOnClickListener {
            errorCallBack.invoke()
        }
        getView().setVisible(View.VISIBLE)
    }

    fun setGoneIfNotNull() {
        noNetwrokView?.setVisible(View.GONE)
    }

}