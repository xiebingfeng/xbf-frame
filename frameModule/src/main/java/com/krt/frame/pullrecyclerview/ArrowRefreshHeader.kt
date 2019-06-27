package com.krt.frame.pullrecyclerview

import android.animation.ValueAnimator
import android.content.Context
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.krt.frame.R

class ArrowRefreshHeader : LinearLayout, BaseRefreshHeader {

    private var mContainer: ConstraintLayout? = null
    private var mStatusTextView: TextView? = null

    private var mMeasuredHeight: Int = 0
//    private var mLoadingView: ImageView? = null

//    private lateinit var mAnimationDrawable: AnimationDrawable

    // 显示进度
    // 显示箭头图片
    var state = BaseRefreshHeader.STATE_NORMAL
        set(state) {
            if (state == this.state) return

            if (state == BaseRefreshHeader.STATE_REFRESHING) {
//                if (mLoadingView != null)
//                    mLoadingView!!.visibility = View.VISIBLE
//                mAnimationDrawable.start()
                smoothScrollTo(mMeasuredHeight, false)
            } else if (state == BaseRefreshHeader.STATE_DONE) {
//                if (mLoadingView != null)
//                    mLoadingView!!.visibility = View.VISIBLE
            } else {
//                if (mLoadingView != null) {
////                    mLoadingView!!.visibility = View.GONE
//                }
            }
            when (state) {
                BaseRefreshHeader.STATE_NORMAL -> {
                    if (this.state == BaseRefreshHeader.STATE_RELEASE_TO_REFRESH) {
                    }
                    if (this.state == BaseRefreshHeader.STATE_REFRESHING) {
                    }
                    mStatusTextView!!.setText(R.string.frame_listview_header_hint_normal)
                }
                BaseRefreshHeader.STATE_RELEASE_TO_REFRESH -> if (this.state != BaseRefreshHeader.STATE_RELEASE_TO_REFRESH) {
                    mStatusTextView!!.setText(R.string.frame_listview_header_hint_release)
                }
                BaseRefreshHeader.STATE_REFRESHING -> mStatusTextView!!.setText(R.string.frame_refreshing)
                BaseRefreshHeader.STATE_DONE -> mStatusTextView!!.setText(R.string.frame_refresh_done)
            }

            field = state
        }

    var visibleHeight: Int
        get() {
            val lp = mContainer!!.layoutParams as LinearLayout.LayoutParams
            return lp.height
        }
        set(height) {
            var height = height
            if (height < 0) height = 0
            val lp = mContainer!!.layoutParams as LinearLayout.LayoutParams
            lp.height = height
            mContainer!!.layoutParams = lp
        }

    constructor(context: Context) : super(context) {
        initView()
    }

    /**
     * @param context
     * @param attrs
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    private fun initView() {
        // 初始情况，设置下拉刷新view高度为0
        mContainer = LayoutInflater.from(context).inflate(
                R.layout.frame_pull_recyclerview_head_view, null
        ) as ConstraintLayout

        addView(mContainer, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0))
        gravity = Gravity.BOTTOM

        mStatusTextView = findViewById<View>(R.id.refresh_status_text_view) as TextView

        //init the progress view
//        mLoadingView = findViewById(R.id.list_view_header_loading)
//        mAnimationDrawable = mLoadingView!!.drawable as AnimationDrawable

        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mMeasuredHeight = resources.getDimension(R.dimen.base_70).toInt()
    }

    fun destroy() {
    }

    override fun refreshComplete(completeImmediately: Boolean) {
        if (state == BaseRefreshHeader.STATE_DONE) {
            return
        }

        state = BaseRefreshHeader.STATE_DONE
//        mAnimationDrawable.stop()

        if (completeImmediately) {
            state = BaseRefreshHeader.STATE_NORMAL
            smoothScrollTo(0, false)
            return
        }

        Handler().postDelayed({
            reset()
        }, 400)
    }

    override fun onMove(delta: Float) {
        if (visibleHeight > 0 || delta > 0) {
            visibleHeight += delta.toInt()
            if (state <= BaseRefreshHeader.STATE_RELEASE_TO_REFRESH) { // 未处于刷新状态，更新箭头
                if (visibleHeight > mMeasuredHeight) {
                    state = BaseRefreshHeader.STATE_RELEASE_TO_REFRESH
                } else {
                    state = BaseRefreshHeader.STATE_NORMAL
                }
            }
        }
    }

    override fun releaseAction(): Boolean {
        var isOnRefresh = false
        val height = visibleHeight
        if (height == 0)
        // not visible.
            isOnRefresh = false

        if (visibleHeight > mMeasuredHeight && state < BaseRefreshHeader.STATE_REFRESHING) {
            state = BaseRefreshHeader.STATE_REFRESHING
            isOnRefresh = true
        }
        // refreshing and header isn't shown fully. do nothing.
        if (state == BaseRefreshHeader.STATE_REFRESHING && height <= mMeasuredHeight) {
            //return;
        }
        if (state != BaseRefreshHeader.STATE_REFRESHING) {
            smoothScrollTo(0, true)
        }

        if (state == BaseRefreshHeader.STATE_REFRESHING) {
            val destHeight = mMeasuredHeight
            smoothScrollTo(destHeight, false)
        }

        return isOnRefresh
    }

    private fun reset() {
        smoothScrollTo(0, true)
        Handler().postDelayed({
            state = BaseRefreshHeader.STATE_NORMAL
        }, 300)
    }

    private fun smoothScrollTo(destHeight: Int, smooth: Boolean) {
        if (visibleHeight == destHeight) {
            return
        }

        if (smooth) {
            val animator = ValueAnimator.ofInt(visibleHeight, destHeight)
            animator.setDuration(300).start()
            animator.addUpdateListener { animation -> visibleHeight = animation.animatedValue as Int }
            animator.start()
        } else {
            visibleHeight = destHeight
        }
    }

    fun setPullHeadBackGround(color: Int) {
        mContainer?.setBackgroundColor(color)
    }

}