package com.krt.frame.pullrecyclerview

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewParent
import com.chad.library.adapter.base.BaseQuickAdapter
import com.krt.frame.pullrecyclerview.BaseRefreshHeader.Companion.STATE_REFRESHING

/**
 * 简易版 下拉刷新，因项目要配合第三方Adapter库，所以不要做太多的定制，只是简单的把功能实现了，
 * 如果需要FootView或加载更多功能，直接用当前项目推荐的Adapter库
 */
open class PullRecyclerView(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {

    private var mLoadingListener: LoadingListener? = null

    private var mRefreshHeader: ArrowRefreshHeader? = null

    private var mLastY = -1f

    private val DRAG_RATE = 2f

    private var appbarState = AppBarStateChangeListener.State.EXPANDED

    var actionIsHeaderShow: (() -> Unit)? = null

    //下面两个 Enabled不一样，一个是当前类控制的，还有一个是全局，通过外部设置的
    //比如有这么一个场景，刷新功能继续使用，但刷新后不能再手动刷新了
    private var pullRefreshEnabled = false

    var globalPullrefreshEnabled = true

    /**
     * 初始化下拉刷新。下拉刷新的View是在Adapter的HeadView上的，所以不管有没数据，必须要先设置Adapter，
     * 等有数据来时再塞数据就行了
     */
    fun initPullRefreshListener(listener: LoadingListener) {
        pullRefreshEnabled = true

        if (null == mRefreshHeader) {
            mRefreshHeader = ArrowRefreshHeader(context)

            if (adapter == null) {
                throw NullPointerException("adapter is null,in PullRecyclerView,must setAdapter first!!!!!!!!!!")
            }

            (adapter as BaseQuickAdapter<*, *>).addHeaderView(mRefreshHeader)
        }
        mLoadingListener = listener
    }

    /**
     * 启动刷新功能
     */
    fun refresh() {
        if (mRefreshHeader?.state == BaseRefreshHeader.STATE_REFRESHING) {
            return
        }
        mRefreshHeader?.state = BaseRefreshHeader.STATE_REFRESHING
        mLoadingListener?.onRefresh()
    }

    /**
     * 刷新完成
     *
     * completeImmediately:立即完成，是否去除完成时的动画效果
     */
    fun refreshComplete(completeImmediately: Boolean) {
        if (mRefreshHeader?.state == BaseRefreshHeader.STATE_DONE) {
            return
        }
        mRefreshHeader?.refreshComplete(completeImmediately)
    }

    /**
     * call it when you finish the activity,
     * when you call this,better don't call some kind of functions like
     * RefreshHeader,because the reference of mHeaderViews is NULL.
     */
    fun destroy() {
        mRefreshHeader?.let {
            it.destroy()
            mRefreshHeader = null
        }

    }

    interface LoadingListener {
        fun onRefresh()
    }

    /**
     * ======================================================= end =======================================================
     */

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (!globalPullrefreshEnabled) {
            return super.onTouchEvent(ev)
        }


        if (mLastY == -1f) {
            mLastY = ev.rawY
        }
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> mLastY = ev.rawY
            MotionEvent.ACTION_MOVE -> {
                val deltaY = ev.rawY - mLastY
                mLastY = ev.rawY
                if (isOnTop() && pullRefreshEnabled && appbarState === AppBarStateChangeListener.State.EXPANDED) {
                    mRefreshHeader?.onMove(deltaY / DRAG_RATE)
                    if (mRefreshHeader!!.visibleHeight > 0 && mRefreshHeader!!.state < STATE_REFRESHING) {

                        actionIsHeaderShow?.invoke()
                        return false
                    }
                }
            }
            else -> {
                mLastY = -1f // reset
                if (isOnTop() && pullRefreshEnabled && appbarState === AppBarStateChangeListener.State.EXPANDED) {
                    if (mRefreshHeader!!.releaseAction()) {
                        if (mLoadingListener != null) {
                            mLoadingListener!!.onRefresh()
                        }
                    }
                }
            }
        }
        return super.onTouchEvent(ev)
    }

    private fun isOnTop(): Boolean {
        return mRefreshHeader?.parent?.parent != null
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        //解决和CollapsingToolbarLayout冲突的问题
        var appBarLayout: AppBarLayout? = null
        var p: ViewParent? = parent
        while (p != null) {
            if (p is CoordinatorLayout) {
                break
            }
            p = p.parent
        }
        if (p is CoordinatorLayout) {
            val coordinatorLayout = p as CoordinatorLayout?
            val childCount = coordinatorLayout!!.childCount
            for (i in childCount - 1 downTo 0) {
                val child = coordinatorLayout.getChildAt(i)
                if (child is AppBarLayout) {
                    appBarLayout = child
                    break
                }
            }
            appBarLayout?.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
                override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
                    appbarState = state
                }
            })
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        this.removeAllViews()
    }

    fun setPullHeadBackGround(color: Int) {
        mRefreshHeader?.setPullHeadBackGround(color)
    }

}