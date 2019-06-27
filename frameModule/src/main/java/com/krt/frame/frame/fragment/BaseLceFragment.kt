package com.krt.frame.frame.fragment

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.NetworkUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.krt.frame.R
import com.krt.frame.app.config.ConfigKeys
import com.krt.frame.app.config.KRT
import com.krt.frame.ext.obs
import com.krt.frame.ext.showToast
import com.krt.frame.frame.dialog.LceLoadingDialog
import com.krt.frame.frame.lce.LceView
import com.krt.frame.frame.lce.LceViewImpl
import com.krt.frame.frame.mvvm.BaseViewModel
import com.krt.frame.frame.mvvm.LCEViewState
import com.krt.frame.frame.mvvm.LceError
import com.krt.frame.pullrecyclerview.AutoSwipeRefreshView
import com.krt.frame.pullrecyclerview.PullRecyclerView
import com.lzy.okgo.OkGo

abstract class BaseLceFragment<VM : BaseViewModel> : BaseVMFragment<VM>(), LceView {

    @Volatile
    private var showContentAfterAnimationEnd = false

    private val mLceView: LceViewImpl by lazy {
        val lceView = LceViewImpl()
        lceView.setErrorOrNoNetWorkCallBackListener {
            onErrorOrNoNetworkRefreshCallBack()
        }
        lceView.actionShowLoadingDialog = {
            it?.showDialog(fragmentManager, activity) {
                OkGo.getInstance().cancelTag(viewModel)
            }
        }
        lceView
    }

    /**
     * 重写这个函数，可以定制LoadingDialog，框架另提交了普通Dialog:DefaultLoadingDialog
     */
    protected open fun initLoadingDialog(): LceLoadingDialog {
        KRT.getConfigurators().getConfiguration<LceLoadingDialog?>(ConfigKeys.LOADING_DIALOG)?.let {
            return it
        }
        return LceLoadingDialog.Factory().create()
    }

    /**
     * 当网络错误或无网络重新加载时，如果当前用的是PullRecyclerView，直接调用默认的
     */
    protected open fun onErrorOrNoNetworkRefreshCallBack() {
        if (onLoadFirstComingData()) {
            return
        }

        val refreshView = view?.findViewById<View>(R.id.frame_lceRefreshView)
        val loadingView = view?.findViewById<View>(R.id.frame_lceRecyclerView)

        if (null != refreshView || null != loadingView) {
            //已经处于无网络或高延迟状态了，提示吧
            if (!NetworkUtils.isConnected()) {
                showToast(R.string.frame_network_is_not_usable)
                showNoNetwork(false)
                return
            }

            val adapter = (loadingView as RecyclerView).adapter
            if (adapter is BaseQuickAdapter<*, *>) {
                adapter.isUseEmpty(false)
            }

            refreshView?.let {
                (refreshView as AutoSwipeRefreshView).refresh()
                return
            }

            loadingView.let {
                (loadingView as PullRecyclerView).refresh()
            }
        }
    }

    /**
     * 并不是所有界面多是列表界面的，
     * 当界面无列表时，生命周期从这里开始加载数据
     * 当出现数据加载出错或网络错误时，从这里重新开始
     *
     * @return 默认为false，当重写后，请返回true,主要是针对错误时的处理
     *
     * ######
     * ######   一般在首屏网络请求时，请把  HttpCall的参数isErrorContainerShow设置为true，当网络请求失败后，可以重新请求
     */
    protected open fun onLoadFirstComingData(): Boolean {
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mLceView?.clear()
    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        isAnimationEnd = true
        if (showContentAfterAnimationEnd) {
            showContent()
        }
    }

    //===================架构===============

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //绑定
        mLceView.initLceView(view as ViewGroup) {
            initLoadingDialog()
        }

        viewModel?.lceViewState?.obs(this) { integer ->
            when (integer) {
                LCEViewState.CONTENT -> showContent()
                LCEViewState.LOADING -> showLoading(false)
                LCEViewState.LOADING_WITH_DIALOG -> showLoading(true)
                LCEViewState.NO_NETWORK -> showNoNetwork(false)
                LCEViewState.NO_NETWORK_WITH_DIALOG -> showNoNetwork(true)
            }
        }
        viewModel?.lceViewStateError?.obs(this) {
            showError(it)
        }

        if (!getToolBarConfig().isLazyInitView) {
            onLoadFirstComingData()
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        if (getToolBarConfig().isLazyInitView) {
            onLoadFirstComingData()
        }
    }

    /**
     * 显示加载界面
     */
    override fun showLoading(showDialog: Boolean) {
        mLceView.showLoading(showDialog)
    }

    override fun showContent() {
        if (isAnimationEnd) {
            mLceView.showContent()
        } else {
            showContentAfterAnimationEnd = true
        }
    }

    override fun showError(lceError: LceError) {
        mLceView.showError(lceError)
    }

    /**
     * 可能在还未启动loadingDialog时就网络错误了
     */
    override fun showNoNetwork(showDialog: Boolean) {
        mLceView.showNoNetwork(showDialog)
    }

}