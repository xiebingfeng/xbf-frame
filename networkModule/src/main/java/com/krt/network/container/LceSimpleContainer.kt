package com.krt.network.container

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.krt.frame.ext.onClick
import com.krt.frame.ext.setVisible
import com.krt.frame.frame.lce.LceView
import com.krt.frame.frame.mvvm.LceError
import com.krt.network.R
import com.lzy.okgo.OkGo


class LceSimpleContainer : FrameLayout, LceView {

    private var mContext: Context? = null

    private var mContentView: View? = null

    private var mLoadingView: View? = null
    private var mErrorView: View? = null
    private var mNoNetworkView: View? = null

    private var mActionRefresh: ((LceView) -> Unit)? = null

    constructor(context: Context) : super(context) {
        mContext = context
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mContext = context
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mContext = context
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val lceView = View.inflate(mContext, R.layout.net_simple_lce_container, null)
        addView(lceView, 0)

        mLoadingView = lceView.findViewById(R.id.lce_loading_view)
        mErrorView = lceView.findViewById(R.id.tv_error_show)
        mNoNetworkView = lceView.findViewById(R.id.tv_no_network)

        mErrorView?.onClick { startRefresh() }
    }

    override fun showLoading(showDialog: Boolean) {
        getContentView().setVisible(View.GONE)

        mLoadingView?.setVisible(View.VISIBLE)
        mErrorView?.setVisible(View.GONE)
        mNoNetworkView?.setVisible(View.GONE)
    }

    override fun showContent() {
        mLoadingView?.setVisible(View.GONE)
        mErrorView?.setVisible(View.GONE)
        mNoNetworkView?.setVisible(View.GONE)

        getContentView().setVisible(View.VISIBLE)
    }

    override fun showError(lceError: LceError) {
        getContentView().setVisible(View.GONE)

        mLoadingView?.setVisible(View.GONE)
        mErrorView?.setVisible(View.VISIBLE)
        mNoNetworkView?.setVisible(View.GONE)

        mErrorView?.onClick {
            startRefresh()
        }
    }

    override fun showNoNetwork(showDialog: Boolean) {
        getContentView().setVisible(View.GONE)

        mLoadingView?.setVisible(View.GONE)
        mErrorView?.setVisible(View.GONE)
        mNoNetworkView?.setVisible(View.VISIBLE)

        mNoNetworkView?.onClick {
            startRefresh()
        }
    }

    private fun getContentView(): View {
        if (childCount == 1) {
            throw  Exception("you must add a content view inside the LceSimpleContainer")
        }
        return getChildAt(childCount - 1)
    }

    fun setOnRefreshListener(function: (LceView) -> Unit) {
        mActionRefresh = function
    }

    fun startRefresh() {
        mActionRefresh?.invoke(this)
    }

    fun cancelNetWork() {
        OkGo.getInstance().cancelTag(this)
    }

}