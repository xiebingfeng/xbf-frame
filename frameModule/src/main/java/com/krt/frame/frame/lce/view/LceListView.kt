package com.krt.frame.frame.lce.view

import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ReflectUtils
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.loadmore.LoadMoreView
import com.krt.frame.R
import com.krt.frame.ext.showToast
import com.krt.frame.frame.mvvm.LceError
import com.krt.frame.pullrecyclerview.PullRecyclerView

class LceListView(private val rootView: ViewGroup) {

    private var loadingView: View? = null

    private val mHandler: Handler?
        get() {
            return Handler()
        }

    fun getView(): View? {

        loadingView?.let { return it }

        loadingView = rootView.findViewById(R.id.frame_lceRefreshView)
        if (loadingView == null) {
            loadingView = rootView.findViewById(R.id.frame_lceRecyclerView)
        }
        return loadingView
    }

    fun showContent() {
        when (loadingView) {
            is SwipeRefreshLayout -> {
                (loadingView as SwipeRefreshLayout).isRefreshing = false
                completePullRecyclerView()
            }
            is PullRecyclerView -> {
                (loadingView as PullRecyclerView).refreshComplete(false)
                completePullRecyclerView()
            }
        }
    }

    fun showLoading(action: () -> Unit): Boolean {
        fun refreshRecyclerView() {
            if (getView() is PullRecyclerView) {
                (getView() as PullRecyclerView).refresh()
            } else {
                (getView() as SwipeRefreshLayout).isRefreshing = true
            }
        }

        when (getView()) {
            is SwipeRefreshLayout, is PullRecyclerView -> {
                val adapter: RecyclerView.Adapter<*>? = if (getView() is PullRecyclerView) {
                    (getView() as RecyclerView).adapter
                } else {
                    getView()?.findViewById<RecyclerView>(R.id.frame_lceRecyclerView)?.adapter
                }

                if (null == adapter) {
                    loadingView = null
                    return false
                }

                if (adapter is BaseQuickAdapter<*, *> && adapter.data.size > 0) {
                    if (ReflectUtils.reflect(adapter).field("mLoadMoreView")?.get<LoadMoreView>()?.loadMoreStatus != LoadMoreView.STATUS_LOADING) {
                        refreshRecyclerView()
                    }
                } else {
                    refreshRecyclerView()
                }
                action.invoke()
                return true
            }
        }
        return false
    }

    fun showNoNetwork() {
        when (getView()) {
            is SwipeRefreshLayout -> {
                (getView() as SwipeRefreshLayout).isRefreshing = false
                completePullRecyclerView()
                if (completeLoadMoreRecyclerView(StringUtils.getString(R.string.frame_network_is_not_usable))) {
                    return
                }
            }
            is PullRecyclerView -> {
                (getView() as PullRecyclerView).refreshComplete(true)
                completePullRecyclerView()
                if (completeLoadMoreRecyclerView(StringUtils.getString(R.string.frame_network_is_not_usable))) {
                    return
                }
            }
        }
    }

    fun showError(lceError: LceError) {
        //当前为列表，且有数据，报错就行了
        when (getView()) {
            is SwipeRefreshLayout -> {
                (getView() as SwipeRefreshLayout).isRefreshing = false
                completePullRecyclerView()
                if (completeLoadMoreRecyclerView(lceError.errorMsg)) {
                    return
                }
            }
            is PullRecyclerView -> {
                (getView() as PullRecyclerView).refreshComplete(true)
                completePullRecyclerView()
                if (completeLoadMoreRecyclerView(lceError.errorMsg)) {
                    return
                }
            }
        }
    }

    fun clear() {
        mHandler?.removeCallbacksAndMessages(null)
    }

    private fun completePullRecyclerView() {
        val adapter: RecyclerView.Adapter<*>
        if (getView() is PullRecyclerView) {
            adapter = (getView() as RecyclerView).adapter!!
        } else {
            adapter = getView()?.findViewById<RecyclerView>(R.id.frame_lceRecyclerView)!!.adapter!!
        }

        if (adapter is BaseQuickAdapter<*, *>) {
            //数据为空
            if (null != adapter.emptyView) {
                if (getView() is PullRecyclerView) {
                    mHandler?.postDelayed({
                        adapter.isUseEmpty(true)
                        getView()?.requestLayout()
                    }, 300)
                } else {
                    adapter.isUseEmpty(true)
                    //因为在http请求时，先加载数据，再显示内容。所以 Adapter已经设置了数据，再调这个的时候。如果内容为空的话，是不会出新界面的。
                    //所以还是看不到EmptyView，所以我要主动刷新一下，因为数据为空，所以刷新时影响不大
                    if (adapter.data.isEmpty()) {
                        mHandler!!.postDelayed({
                            if (adapter.data.isEmpty()) {
                                adapter.notifyDataSetChanged()
                            }
                        }, 50)
                    }
                }
            }

            //Foot View
            if (adapter.isLoading) {
                adapter.loadMoreComplete()
            }
        }
    }

    private fun completeLoadMoreRecyclerView(toastString: String?): Boolean {
        val adapter = if (getView() is PullRecyclerView) {
            (getView() as RecyclerView).adapter!!
        } else {
            getView()?.findViewById<RecyclerView>(R.id.frame_lceRecyclerView)!!.adapter!!
        }
        if (adapter is BaseQuickAdapter<*, *> && adapter.data.size > 0) {
            adapter.setEnableLoadMore(false)
            mHandler?.removeCallbacksAndMessages(null)
            mHandler?.postDelayed({
                adapter.setEnableLoadMore(true)
            }, 500)

            toastString?.let {
                showToast(it)
            }
            return true
        }
        return false
    }

}