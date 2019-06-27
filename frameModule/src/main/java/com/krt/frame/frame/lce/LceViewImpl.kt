package com.krt.frame.frame.lce


import android.view.View
import android.view.ViewGroup
import com.krt.frame.R
import com.krt.frame.ext.setVisible
import com.krt.frame.frame.dialog.LceLoadingDialog
import com.krt.frame.frame.lce.view.*
import com.krt.frame.frame.mvvm.LceError

class LceViewImpl : LceView {

    /**
     * 显示内容的
     */
    private lateinit var contentView: View
    private lateinit var rootView: ViewGroup

    private var lceRootView: View? = null

    private val mLceNormalLoadingView by lazy { LceNormalLoadingView(rootView) }
    private val mLceErrorView by lazy { LceErrorView(rootView) }
    private val mLceNoNetworkView by lazy { LceNetworkView(rootView) }
    private val mLceListView by lazy { LceListView(rootView) }

    private val mLceLoadingDialogManager by lazy { LceLoadingDialogManager() }

    private var actionInitLoadingDialog: (() -> LceLoadingDialog)? = null

    private lateinit var errorCallBack: () -> Unit

    fun setErrorOrNoNetWorkCallBackListener(action: () -> Unit) {
        this.errorCallBack = action
    }

    var actionShowLoadingDialog: ((LceLoadingDialog?) -> Unit)? = null

    /**
     * 目前只对Fragment的Lce功能得到充分智齿，Activity的只以Dialog模式展示
     *
     * @param rootView
     * @param actionInitLoadingDialog
     */
    fun initLceView(rootView: ViewGroup?, actionInitLoadingDialog: () -> LceLoadingDialog) {
        rootView?.let {
            this.rootView = rootView
            contentView = rootView.findViewById(R.id.core_contentView)
        }
        this.actionInitLoadingDialog = actionInitLoadingDialog
    }

    override fun showLoading(showDialog: Boolean) {
        if (showDialog) {
            val dialog = mLceLoadingDialogManager.getLoadingDialog(actionInitLoadingDialog)
            actionShowLoadingDialog?.invoke(dialog)
            return
        }

        showLceContainerVisible(true)
        mLceErrorView.setGoneIfNotNull()
        mLceNoNetworkView.setGoneIfNotNull()

        val listLoadingResult = mLceListView.showLoading {
            contentView.setVisible(View.VISIBLE)
        }

        if (!listLoadingResult) {
            contentView.setVisible(View.INVISIBLE)
            mLceNormalLoadingView.showLoading()
        }
    }

    override fun showContent() {
        if (mLceLoadingDialogManager.isLoadingDialogNotNull()) {
            mLceLoadingDialogManager.showContent()
            return
        }

        contentView.setVisible(View.VISIBLE)
        showLceContainerVisible(false)
        mLceErrorView.setGoneIfNotNull()
        mLceNoNetworkView.setGoneIfNotNull()

        mLceListView.showContent()
        mLceNormalLoadingView.setGoneIfNotNull()
    }

    override fun showError(lceError: LceError) {
        if (mLceLoadingDialogManager.isLoadingDialogNotNull()) {
            mLceLoadingDialogManager.showError(lceError)
            if (!lceError.errorContainerShow) {
                return
            }
        }

        mLceListView.showError(lceError)
        mLceNormalLoadingView.setGoneIfNotNull()

        contentView.setVisible(View.GONE)

        showLceContainerVisible(true)
        mLceNoNetworkView.setGoneIfNotNull()
        mLceErrorView.initFunction(errorCallBack, lceError)
    }

    override fun showNoNetwork(showDialog: Boolean) {
        if (mLceLoadingDialogManager.isLoadingDialogNotNull() || showDialog) {
            mLceLoadingDialogManager.showNoNetwork(showDialog)
            return
        }
        mLceListView.showNoNetwork()
        mLceNormalLoadingView.setGoneIfNotNull()

        contentView.setVisible(View.GONE)
        showLceContainerVisible(true)
        mLceErrorView.setGoneIfNotNull()
        mLceNoNetworkView.initFunction(errorCallBack)
    }

    private fun showLceContainerVisible(visible: Boolean) {
        if (visible) {
            if (lceRootView == null) {
                val rootView = contentView.parent as View
                lceRootView = View.inflate(rootView.context, R.layout.frame_default_lce_view, rootView.findViewById(R.id.lce_container_view))
            }
            lceRootView?.setVisible(View.VISIBLE)
        } else {
            lceRootView?.setVisible(View.GONE)
        }
    }

    fun clear() {
        mLceLoadingDialogManager.dismiss()
        mLceListView.clear()
    }

}
