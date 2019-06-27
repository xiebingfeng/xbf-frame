package com.krt.frame.frame.lce.view

import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import com.krt.frame.R
import com.krt.frame.ext.setVisible

class LceNormalLoadingView(private val rootView: ViewGroup) {

    private var mLoadingViewStub: ViewStub? = null

    private var mLoadingView: View? = null

    fun getLoadingView(): View {
        if (mLoadingView == null) {
            mLoadingViewStub = rootView.findViewById(R.id.lce_loading_view)
            mLoadingView = mLoadingViewStub!!.inflate()
        }
        return mLoadingView!!
    }

    fun setGoneIfNotNull() {
        mLoadingView?.setVisible(View.GONE)
    }

    fun showLoading() {
        getLoadingView().setVisible(View.VISIBLE)
    }

}