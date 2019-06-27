package com.krt.frame.frame.toolbar.manager

import android.support.constraint.ConstraintSet
import android.view.View
import com.krt.frame.R
import com.krt.frame.frame.toolbar.ToolBarConfigHelper

class ToolBarConfigHelperProxy(private val toolBarConfigHelper: ToolBarConfigHelper) {

    fun addContainerRightLeftView(viewAdd: View) {
        val rootView = toolBarConfigHelper.getToolBar()
        rootView.addView(viewAdd)
        val viewAddId = viewAdd.id

        val constrainSet = ConstraintSet()
        constrainSet.clone(rootView)
        constrainSet.constrainWidth(viewAddId, 0)
        constrainSet.constrainHeight(viewAddId, ConstraintSet.MATCH_CONSTRAINT)

        constrainSet.connect(
                viewAddId,
                ConstraintSet.LEFT,
                R.id.frame_toolbar_left_view,
                ConstraintSet.RIGHT,
                0
        )

        constrainSet.connect(
                viewAddId,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                0
        )
        constrainSet.connect(
                viewAddId,
                ConstraintSet.RIGHT,
                ConstraintSet.PARENT_ID,
                ConstraintSet.RIGHT,
                0
        )
        constrainSet.connect(
                viewAddId,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM,
                0
        )
        constrainSet.applyTo(rootView)
    }

}