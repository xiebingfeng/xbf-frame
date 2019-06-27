package com.krt.frame.frame.toolbar.manager

import android.support.constraint.ConstraintLayout
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.krt.frame.R
import com.krt.frame.ext.onClick
import com.krt.frame.ext.setVisible
import com.krt.frame.frame.toolbar.ToolBarConfig
import com.krt.frame.frame.toolbar.style.ToolBarStyle
import com.krt.frame.frame.toolbar.style.ToolBarViewStyle

class ToolBarProxy {

    private lateinit var mRootView: ConstraintLayout
    private lateinit var mToolBarConfig: ToolBarConfig

    private var mImageViewBackground: ImageView? = null

    private var mBottomLine: View? = null
    private var mLeftView: View? = null
    private var mLeft2View: View? = null
    private var mRightView: View? = null
    private var mRight2View: View? = null

    fun init(rootView: View, toolBarConfig: ToolBarConfig) {
        val toolbar = rootView.findViewById<Toolbar>(R.id.core_toolBar)
        mRootView = rootView.findViewById(R.id.tool_bar_container)
        mToolBarConfig = toolBarConfig

        //初始化 主布局属性
        if (!initContent(toolBarConfig, rootView, toolbar)) {
            return
        }

        //初始化toolBar
        initToolBar(toolbar)
        initMiddleView()
        initLeftView()
        initLeft2View()
        initRightView()
        initRight2View()
    }

    /**
     * 初始化主布局属性
     */
    private fun initContent(toolBarConfig: ToolBarConfig?, rootView: View, toolbar: Toolbar): Boolean {
        toolBarConfig?.apply {
            contentBackgroundRes?.let {
                rootView.findViewById<AppCompatImageView>(R.id.core_background).apply {
                    setImageResource(it)
                    scaleType = contentBackgroundScaleType
                }
            }
        }

        if (null == toolBarConfig || toolBarConfig.toolBarStyle == ToolBarStyle.NONE) {
            toolbar.visibility = View.GONE
            return false
        }
        return true
    }

    /**
     * 初始化toolBar
     */
    private fun initToolBar(toolbar: Toolbar) {
        with(mToolBarConfig)
        {
            toolBarBackgroundColor?.let {
                mRootView.setBackgroundColor(it)
            }

            toolBarBackgroundImageViewRes?.let {
                if (null == mImageViewBackground) {
                    mImageViewBackground = ToolBarViewInitManager.initToolBarBackgroundImageView(mRootView)
                }

                mImageViewBackground?.setImageResource(it)
            }

            toolBarBottomLineVisible?.let {
                if (null == mBottomLine) {
                    mBottomLine = ToolBarViewInitManager.initToolBarBottomLine(mRootView)
                }

            }
        }

        //        app:layout_scrollFlags="scroll|enterAlways|snap"
        //沉浸式
        if (mToolBarConfig.toolBarStyle === ToolBarStyle.IMMERSE) {
            val mParams = toolbar.layoutParams as AppBarLayout.LayoutParams
            mParams.scrollFlags = 5
        } else if (mToolBarConfig.toolBarStyle === ToolBarStyle.NORMAL) {

        }

    }

    /**
     * middle title
     */
    private fun initMiddleView() {
        if (mToolBarConfig.toolBarStyle === ToolBarStyle.NONE) {
            return
        }

        with(mToolBarConfig)
        {
            val titleView = mRootView.findViewById<AppCompatTextView>(R.id.tv_tool_bar_title)

            middleTitle?.let {
                titleView.text = it
            }

            middleTitleRes?.let {
                titleView.text = titleView.resources.getString(it)
            }

            middleTitleClickListener?.let {
                titleView.onClick {
                    middleTitleClickListener!!.invoke(titleView)
                }
            }

            middleTitleFontColor?.let {
                titleView.setTextColor(it)
            }

            titleView?.setVisible(mToolBarConfig.middleTitleDefaultVisible)
        }
    }

    /**
     * left View
     */
    private fun initLeftView() {
        if (mToolBarConfig.leftViewStyle == ToolBarViewStyle.NONE) {
            return
        }

        if (mToolBarConfig.leftViewStyle == ToolBarViewStyle.ICON) {
            if (null == mLeftView) {
                mLeftView = ToolBarViewInitManager.initLeftImageButton(mRootView, mBottomLine)
            }

            with(mToolBarConfig)
            {
                leftViewIcon?.let {
                    (mLeftView as ImageButton).setImageResource(it)
                }

                leftViewBackGround?.let {
                    mLeftView?.setBackgroundResource(it)
                }

                leftViewClickListener?.let {
                    mLeftView?.onClick {
                        leftViewClickListener?.invoke(mLeftView!!)
                    }
                }
                mLeftView?.setVisible(mToolBarConfig.leftViewDefaultVisible)
            }
            return
        }

        if (mToolBarConfig.leftViewStyle == ToolBarViewStyle.TEXT) {
            if (null == mLeftView) {
                mLeftView = ToolBarViewInitManager.initLeftTextView(mRootView, mBottomLine)
            }


            with(mToolBarConfig)
            {
                leftViewText?.let {
                    (mLeftView as TextView).text = it
                }

                leftViewTextFontColor?.let {
                    (mLeftView as TextView).setTextColor(it)
                }

                leftViewBackGround?.let {
                    (mLeftView as TextView).setBackgroundResource(it)
                }

                leftViewClickListener?.let {
                    (mLeftView as TextView).onClick {
                        leftViewClickListener?.invoke((mLeftView as TextView))
                    }
                }
                mLeftView?.setVisible(mToolBarConfig.leftViewDefaultVisible)
            }
        }
    }

    /**
     * left2 View
     */
    private fun initLeft2View() {
        if (mToolBarConfig.left2ViewStyle === ToolBarViewStyle.NONE) {
            return
        }

        if (mToolBarConfig.left2ViewStyle === ToolBarViewStyle.ICON) {
            if (null == mLeft2View) {
                mLeft2View = ToolBarViewInitManager.initLeft2ImageButton(mRootView, mBottomLine, mLeftView)
            }

            with(mToolBarConfig)
            {

                left2ViewIcon?.let {
                    (mLeft2View as ImageButton).setImageResource(it)
                }

                left2ViewBackGround?.let {
                    (mLeft2View as ImageButton).setBackgroundResource(it)
                }

                left2ViewClickListener?.let {
                    (mLeft2View as ImageButton).onClick {
                        left2ViewClickListener!!.invoke((mLeft2View as ImageButton))
                    }
                }
                mLeft2View?.setVisible(mToolBarConfig.left2ViewDefaultVisible)
            }

            return
        }

        //判断右1是哪个View在显示状态，通过代码动态调整它们之间的关系
        if (mToolBarConfig.left2ViewStyle === ToolBarViewStyle.TEXT) {
            if (null == mLeft2View) {
                mLeft2View = ToolBarViewInitManager.initLeft2TextView(mRootView, mBottomLine, mLeftView)
            }


            with(mToolBarConfig)
            {
                left2ViewText?.let {
                    (mLeft2View as TextView).text = it
                }

                left2ViewTextFontColor?.let {
                    (mLeft2View as TextView).setTextColor(it)
                }

                left2ViewBackGround?.let {
                    (mLeft2View as TextView).setBackgroundResource(it)
                }

                left2ViewClickListener?.let {
                    (mLeft2View as TextView).onClick {
                        left2ViewClickListener!!.invoke((mLeft2View as TextView))
                    }
                }
                mLeft2View?.setVisible(mToolBarConfig.left2ViewDefaultVisible)
            }
        }
    }


    /**
     * right View
     */
    private fun initRightView() {
        if (mToolBarConfig.rightViewStyle === ToolBarViewStyle.NONE) {
            return
        }

        if (mToolBarConfig.rightViewStyle === ToolBarViewStyle.ICON) {
            if (null == mRightView) {
                mRightView = ToolBarViewInitManager.initRightImageButton(mRootView, mBottomLine)
            }


            with(mToolBarConfig)
            {

                rightViewIcon?.let {
                    (mRightView as ImageButton).setImageResource(it)
                }

                rightViewBackGround?.let {
                    (mRightView as ImageButton).setBackgroundResource(it)
                }

                rightViewClickListener?.let {
                    (mRightView as ImageButton).onClick {
                        rightViewClickListener?.invoke((mRightView as ImageButton))
                    }
                }
                mRightView?.setVisible(mToolBarConfig.rightViewDefaultVisible)
            }

            return
        }

        if (mToolBarConfig.rightViewStyle === ToolBarViewStyle.TEXT) {
            if (null == mRightView) {
                mRightView = ToolBarViewInitManager.initRightTextView(mRootView, mBottomLine)
            }

            with(mToolBarConfig)
            {
                rightViewText?.let {
                    (mRightView as TextView).text = mToolBarConfig.rightViewText
                }

                rightViewTextFontColor?.let {
                    (mRightView as TextView).setTextColor(it)
                }

                rightViewBackGround?.let {
                    (mRightView as TextView).setBackgroundResource(it)
                }

                rightViewClickListener?.let {
                    (mRightView as TextView).onClick {
                        rightViewClickListener?.invoke((mRightView as TextView))
                    }
                }
                mRightView?.setVisible(mToolBarConfig.rightViewDefaultVisible)
            }
        }
    }

    /**
     * right2 View
     */
    private fun initRight2View() {
        if (mToolBarConfig.right2ViewStyle === ToolBarViewStyle.NONE) {
            return
        }

        if (mToolBarConfig.right2ViewStyle === ToolBarViewStyle.ICON) {
            if (null == mRight2View) {
                mRight2View = ToolBarViewInitManager.initRight2ImageButton(mRootView, mBottomLine, mRightView)
            }

            with(mToolBarConfig)
            {

                right2ViewIcon?.let {
                    (mRight2View as ImageButton).setImageResource(it)
                }

                right2ViewBackGround?.let {
                    (mRight2View as ImageButton).setBackgroundResource(it)
                }

                right2ViewClickListener?.let {
                    (mRight2View as ImageButton).onClick {
                        right2ViewClickListener!!.invoke((mRight2View as ImageButton))
                    }
                }
                mRight2View?.setVisible(mToolBarConfig.right2ViewDefaultVisible)
            }

            return
        }

        //判断右1是哪个View在显示状态，通过代码动态调整它们之间的关系
        if (mToolBarConfig.right2ViewStyle === ToolBarViewStyle.TEXT) {
            if (null == mRight2View) {
                mRight2View = ToolBarViewInitManager.initRight2TextView(mRootView, mBottomLine, mRightView)
            }


            with(mToolBarConfig)
            {
                right2ViewText?.let {
                    (mRight2View as TextView).text = it
                }

                right2ViewTextFontColor?.let {
                    (mRight2View as TextView).setTextColor(it)
                }

                right2ViewBackGround?.let {
                    (mRight2View as TextView).setBackgroundResource(it)
                }

                right2ViewClickListener?.let {
                    (mRight2View as TextView).onClick {
                        right2ViewClickListener!!.invoke((mRight2View as TextView))
                    }
                }
                mRight2View?.setVisible(mToolBarConfig.right2ViewDefaultVisible)
            }
        }
    }
}