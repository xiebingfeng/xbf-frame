package com.krt.frame.frame.toolbar


import android.support.annotation.ColorInt
import android.view.View
import android.widget.ImageView
import com.krt.frame.frame.toolbar.style.ToolBarStyle
import com.krt.frame.frame.toolbar.style.ToolBarViewStyle
import me.yokeyword.fragmentation.SupportFragment

class ToolBarConfig(

        //跟ToolBar关系不大，是主View的背影图，为了方便才放在这里
        var layoutId: Int,
        var isLazyInitView: Boolean = false,   //是否延迟初始化，也就是当界面真正显示的时候才加载数据
        var contentBackgroundRes: Int? = null,
        @ColorInt var contentBackgroundColor: Int? = null,
        var contentBackgroundScaleType: ImageView.ScaleType = ImageView.ScaleType.FIT_START,
        var contentSwipeBackEnabled: Boolean = false,
        var autoCheckEventBus: Boolean = true,    //自动检测EventBus，注册和销毁
        var openNetWorkChange: Boolean = false,

        /**
         * toolBar
         */
        var toolBarStyle: ToolBarStyle = ToolBarStyle.NONE,
        @ColorInt var toolBarBackgroundColor: Int? = null,
        @ColorInt var toolBarBackgroundImageViewRes: Int? = null,
        var toolBarBottomLineVisible: Boolean? = null,

        /**
         * middle title
         */
        var middleTitle: String? = null,
        var middleTitleRes: Int? = null,
        var middleTitleFontColor: Int? = null,
        var middleTitleClickListener: ((View) -> Unit)? = null,
        var middleTitleDefaultVisible: Int = View.VISIBLE,
        /**
         * left view
         */
        var leftViewStyle: ToolBarViewStyle? = null,
        var leftViewIcon: Int? = null,
        var leftViewBackGround: Int? = null,
        var leftViewText: String? = "",
        var leftViewTextFontColor: Int? = null,
        var leftViewClickListener: ((View) -> Unit)? = null,
        var leftViewDefaultVisible: Int = View.VISIBLE,
        /**
         * left2 view
         */
        var left2ViewStyle: ToolBarViewStyle? = null,
        var left2ViewIcon: Int? = null,
        var left2ViewBackGround: Int? = null,
        var left2ViewText: String? = "",
        var left2ViewTextFontColor: Int? = null,
        var left2ViewClickListener: ((View) -> Unit)? = null,
        var left2ViewDefaultVisible: Int = View.VISIBLE,
        /**
         * right view
         */
        var rightViewStyle: ToolBarViewStyle = ToolBarViewStyle.NONE,
        var rightViewIcon: Int? = null,
        var rightViewBackGround: Int? = null,
        var rightViewText: String? = null,
        var rightViewTextFontColor: Int? = null,
        var rightViewClickListener: ((View) -> Unit)? = null,
        var rightViewDefaultVisible: Int = View.VISIBLE,
        /**
         * right2 view
         */
        var right2ViewStyle: ToolBarViewStyle = ToolBarViewStyle.NONE,
        var right2ViewIcon: Int? = null,
        var right2ViewBackGround: Int? = null,
        var right2ViewText: String? = null,
        var right2ViewTextFontColor: Int? = null,
        var right2ViewClickListener: ((View) -> Unit)? = null,
        var right2ViewDefaultVisible: Int = View.VISIBLE,
        /**
         * 默认行为，简化反复操作，如果设置为true，会在初始化时替代之前做的所有操作
         */
        var currentFragment: SupportFragment? = null
)
