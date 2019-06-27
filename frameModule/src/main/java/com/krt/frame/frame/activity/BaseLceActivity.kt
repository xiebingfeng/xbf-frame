package com.krt.frame.frame.activity

import android.os.Bundle
import com.krt.frame.ext.obs
import com.krt.frame.frame.dialog.LceLoadingDialog
import com.krt.frame.frame.lce.LceView
import com.krt.frame.frame.lce.LceViewImpl
import com.krt.frame.frame.mvvm.BaseViewModel
import com.krt.frame.frame.mvvm.LCEViewState
import com.krt.frame.frame.mvvm.LceError
import com.lzy.okgo.OkGo

abstract class BaseLceActivity<VM : BaseViewModel> : BaseVMActivity<VM>(), LceView {

    /**
     * 重写这个函数，可以定制LoadingDialog，框架另提交了普通Dialog:DefaultLoadingDialog
     */
    protected open fun initLoadingDialog(): LceLoadingDialog {
        return LceLoadingDialog.Factory().create(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        //绑定
        mLceView.initLceView(null) {
            initLoadingDialog()
        }
    }

    override fun showLoading(showDialog: Boolean) {
        mLceView.showLoading(showDialog)
    }

    override fun showContent() {
        mLceView.showContent()
    }

    override fun showError(lceError: LceError) {
        mLceView.showError(lceError)
    }

    override fun showNoNetwork(showDialog: Boolean) {
        mLceView.showNoNetwork(showDialog)
    }

    private val mLceView: LceViewImpl by lazy {
        val lceView = LceViewImpl()
        lceView.actionShowLoadingDialog = {
            it?.showDialog(null, this) {
                OkGo.getInstance().cancelTag(viewModel)
            }
        }
        lceView
    }

}