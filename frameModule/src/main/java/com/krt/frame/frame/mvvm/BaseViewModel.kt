package com.krt.frame.frame.mvvm


import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ThreadUtils
import com.krt.frame.frame.lce.LceView
import com.lzy.okgo.OkGo
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel(application: Application) : AndroidViewModel(application), LceView {

    /**
     * LCE状态,BaseLceFragment监听这个，来动态显示 View
     */
    val lceViewState = MutableLiveData<Int>()

    val lceViewStateError = MutableLiveData<LceError>()

    private var compositeDisposable: CompositeDisposable? = null

    override fun showContent() {
        if (ThreadUtils.isMainThread()) {
            lceViewState.value = LCEViewState.CONTENT
        } else {
            lceViewState.postValue(LCEViewState.CONTENT)
        }
    }

    override fun showLoading(showDialog: Boolean) {
        lceViewState.value = if (showDialog) LCEViewState.LOADING_WITH_DIALOG else LCEViewState.LOADING
    }

    override fun showError(lceError: LceError) {
        lceViewStateError.value = lceError
    }

    override fun showNoNetwork(showDialog: Boolean) {
        lceViewState.value = if (showDialog) LCEViewState.NO_NETWORK_WITH_DIALOG else LCEViewState.NO_NETWORK
    }

    /**
     * 配合RxJava用的
     */
    fun addDisposable(disposable: Disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable?.add(disposable)
    }

    protected fun getCompositeDisposable(): CompositeDisposable {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        return compositeDisposable!!
    }

    override fun onCleared() {
        cancelAllNetWorkByViewModel()
        compositeDisposable?.dispose()
    }

    protected fun cancelAllNetWorkByViewModel() {
        OkGo.getInstance().cancelTag(this)
        //界面销毁的时候，调用下面这个，其实也没啥用。主要还是界面未销毁时，主动取消网络请求的操作
        showContent()
    }

}
