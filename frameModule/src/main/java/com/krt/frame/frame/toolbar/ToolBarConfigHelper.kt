package com.krt.frame.frame.toolbar

import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.TextView
import com.krt.frame.R
import com.krt.frame.frame.toolbar.manager.ToolBarConfigHelperProxy

class ToolBarConfigHelper(private val viewGroup: ConstraintLayout) {

    private val mToolBarConfigHelperProxy by lazy {
        ToolBarConfigHelperProxy(this)
    }

    fun getTitleView(): TextView = viewGroup.findViewById(R.id.tv_tool_bar_title)

    fun getToolBarContainer(): ConstraintLayout = viewGroup.findViewById(R.id.tool_bar_container)

    fun getLeftView() = viewGroup.findViewById(R.id.frame_toolbar_left_view) as? View

    fun getLeft2View() = viewGroup.findViewById(R.id.frame_toolbar_left_2_view) as? View

    fun getRightTextView() = viewGroup.findViewById(R.id.frame_toolbar_right_view) as? TextView

    fun getRightView() = viewGroup.findViewById<View>(R.id.frame_toolbar_right_view)

    fun getRight2View() = viewGroup.findViewById<View>(R.id.frame_toolbar_right_2_view)

    fun getToolBar(): ConstraintLayout = viewGroup.findViewById(R.id.tool_bar_container)

    /**
     * 添加一个容器到左边按钮的右边，默认容器扩充剩余空间
     */
    fun addContainerRightLeftView(viewAdd: View) {
        mToolBarConfigHelperProxy.addContainerRightLeftView(viewAdd)
    }
}