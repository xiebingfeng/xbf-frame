package com.krt.frame.frame.toolbar.manager

import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.krt.frame.R
import com.krt.frame.app.config.KRT

object ToolBarViewInitManager {

    /**
     * 初始化ToolBar背景的ImageView
     */
    fun initToolBarBottomLine(rootView: ConstraintLayout): View {
        val bottomLine = View(rootView.context)
        bottomLine.id = R.id.frame_toolbar_bottom_line
        bottomLine.setBackgroundResource(R.color.base_toolbar_line_color)

        rootView.addView(bottomLine)

        val constrainSet = ConstraintSet()
        constrainSet.clone(rootView)
        constrainSet.constrainWidth(R.id.frame_toolbar_bottom_line, ConstraintSet.MATCH_CONSTRAINT)
        constrainSet.constrainHeight(
                R.id.frame_toolbar_bottom_line,
                rootView.context.resources.getDimensionPixelOffset(R.dimen.base_1)
        )
        constrainSet.connect(
                R.id.frame_toolbar_bottom_line,
                ConstraintSet.LEFT,
                ConstraintSet.PARENT_ID,
                ConstraintSet.LEFT,
                0
        )
        constrainSet.connect(
                R.id.frame_toolbar_bottom_line,
                ConstraintSet.RIGHT,
                ConstraintSet.PARENT_ID,
                ConstraintSet.RIGHT,
                0
        )
        constrainSet.connect(
                R.id.frame_toolbar_bottom_line,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM,
                0
        )
        constrainSet.applyTo(rootView)
        return bottomLine
    }


    /**
     * 初始化ToolBar背景的ImageView
     */
    fun initToolBarBackgroundImageView(rootView: ConstraintLayout): ImageView {
        val imageViewBackground = ImageView(rootView.context)
        imageViewBackground.id = R.id.frame_toolbar_image_background

        rootView.addView(imageViewBackground, 0)

        val constrainSet = ConstraintSet()
        constrainSet.clone(rootView)
        constrainSet.constrainWidth(R.id.frame_toolbar_image_background, ConstraintSet.MATCH_CONSTRAINT)
        constrainSet.constrainHeight(R.id.frame_toolbar_image_background, ConstraintSet.MATCH_CONSTRAINT)
        constrainSet.connect(
                R.id.frame_toolbar_image_background,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                0
        )
        constrainSet.connect(
                R.id.frame_toolbar_image_background,
                ConstraintSet.LEFT,
                ConstraintSet.PARENT_ID,
                ConstraintSet.LEFT,
                0
        )
        constrainSet.connect(
                R.id.frame_toolbar_image_background,
                ConstraintSet.RIGHT,
                ConstraintSet.PARENT_ID,
                ConstraintSet.RIGHT,
                0
        )
        constrainSet.connect(
                R.id.frame_toolbar_image_background,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM,
                0
        )
        constrainSet.applyTo(rootView)
        return imageViewBackground
    }

    /**
     * 初始化leftImageButton
     */
    fun initLeftImageButton(rootView: ConstraintLayout, bottomLine: View?): ImageButton {
        val leftImageButton = View.inflate(rootView.context, R.layout.frame_toolbar_image_button, null) as ImageButton
        leftImageButton.id = R.id.frame_toolbar_left_view
        leftImageButton.setBackgroundResource(R.drawable.base_transparent_click_bg)

        if (bottomLine == null) {
            rootView.addView(leftImageButton)
        } else {
            rootView.addView(leftImageButton, rootView.childCount - 1)
        }

        val constrainSet = ConstraintSet()
        constrainSet.clone(rootView)
        constrainSet.constrainWidth(R.id.frame_toolbar_left_view, 0)
        constrainSet.constrainHeight(R.id.frame_toolbar_left_view, ConstraintSet.MATCH_CONSTRAINT)
        constrainSet.connect(
                R.id.frame_toolbar_left_view,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                0
        )
        constrainSet.connect(
                R.id.frame_toolbar_left_view,
                ConstraintSet.LEFT,
                ConstraintSet.PARENT_ID,
                ConstraintSet.LEFT,
                0
        )
        constrainSet.connect(
                R.id.frame_toolbar_left_view,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM,
                0
        )
        constrainSet.setDimensionRatio(R.id.frame_toolbar_left_view, "1:1")
        constrainSet.applyTo(rootView)
        return leftImageButton
    }

    /**
     * 初始化leftTextView
     */
    fun initLeftTextView(rootView: ConstraintLayout, bottomLine: View?): TextView {
        val leftTextView = View.inflate(rootView.context, R.layout.frame_toolbar_text_view, null) as TextView
        leftTextView.id = R.id.frame_toolbar_left_view
        leftTextView.setTextColor(ContextCompat.getColor(KRT.getApplicationContext(), R.color.base_toolbar_left_button_font_color))

        if (bottomLine == null) {
            rootView.addView(leftTextView)
        } else {
            rootView.addView(leftTextView, rootView.childCount - 1)
        }

        val constrainSet = ConstraintSet()
        constrainSet.clone(rootView)
        constrainSet.constrainWidth(R.id.frame_toolbar_left_view, ConstraintSet.WRAP_CONTENT)
        constrainSet.constrainHeight(R.id.frame_toolbar_left_view, ConstraintSet.MATCH_CONSTRAINT)
        constrainSet.connect(
                R.id.frame_toolbar_left_view,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                0
        )
        constrainSet.connect(
                R.id.frame_toolbar_left_view,
                ConstraintSet.LEFT,
                ConstraintSet.PARENT_ID,
                ConstraintSet.LEFT,
                0
        )
        constrainSet.connect(
                R.id.frame_toolbar_left_view,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM,
                0
        )
        constrainSet.applyTo(rootView)
        return leftTextView
    }

    /**
     * 初始化left2TextView
     */
    fun initLeft2TextView(rootView: ConstraintLayout, bottomLine: View?, leftView: View?): TextView {
        val left2TextView = View.inflate(rootView.context, R.layout.frame_toolbar_text_view, null) as TextView
        left2TextView.id = R.id.frame_toolbar_left_2_view
        left2TextView.setTextColor(ContextCompat.getColor(KRT.getApplicationContext(), R.color.base_toolbar_left_button_font_color))

        if (bottomLine == null) {
            rootView.addView(left2TextView)
        } else {
            rootView.addView(left2TextView, rootView.childCount - 1)
        }

        val constrainSet = ConstraintSet()
        constrainSet.clone(rootView)
        constrainSet.constrainWidth(R.id.frame_toolbar_left_2_view, ConstraintSet.WRAP_CONTENT)
        constrainSet.constrainHeight(R.id.frame_toolbar_left_2_view, ConstraintSet.MATCH_CONSTRAINT)
        constrainSet.connect(
                R.id.frame_toolbar_left_2_view,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                0
        )

        if (null == leftView) {
            constrainSet.connect(
                    R.id.frame_toolbar_left_2_view,
                    ConstraintSet.LEFT,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.RIGHT,
                    0
            )
        } else {
            constrainSet.connect(
                    R.id.frame_toolbar_left_2_view,
                    ConstraintSet.LEFT,
                    leftView.id,
                    ConstraintSet.RIGHT,
                    0
            )
        }

        constrainSet.connect(
                R.id.frame_toolbar_left_2_view,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM,
                0
        )
        constrainSet.applyTo(rootView)
        return left2TextView
    }

    /**
     * 初始化left2TextView
     */
    fun initLeft2ImageButton(rootView: ConstraintLayout, bottomLine: View?, leftView: View?): ImageButton {
        val left2ImageButton = View.inflate(rootView.context, R.layout.frame_toolbar_image_button, null) as ImageButton
        left2ImageButton.id = R.id.frame_toolbar_left_2_view

        if (bottomLine == null) {
            rootView.addView(left2ImageButton)
        } else {
            rootView.addView(left2ImageButton, rootView.childCount - 1)
        }

        val constrainSet = ConstraintSet()
        constrainSet.clone(rootView)
        constrainSet.constrainWidth(R.id.frame_toolbar_left_2_view, ConstraintSet.WRAP_CONTENT)
        constrainSet.constrainHeight(R.id.frame_toolbar_left_2_view, ConstraintSet.MATCH_CONSTRAINT)
        constrainSet.connect(
                R.id.frame_toolbar_left_2_view,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                0
        )

        if (null == leftView) {
            constrainSet.connect(
                    R.id.frame_toolbar_left_2_view,
                    ConstraintSet.LEFT,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.RIGHT,
                    0
            )
        } else {
            constrainSet.connect(
                    R.id.frame_toolbar_left_2_view,
                    ConstraintSet.LEFT,
                    leftView.id,
                    ConstraintSet.RIGHT,
                    0
            )
        }

        constrainSet.connect(
                R.id.frame_toolbar_left_2_view,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM,
                0
        )
        constrainSet.applyTo(rootView)
        return left2ImageButton
    }

    /**
     * 初始化rightImageButton
     */
    fun initRightImageButton(rootView: ConstraintLayout, bottomLine: View?): ImageButton {
        val leftImageButton = View.inflate(rootView.context, R.layout.frame_toolbar_image_button, null) as ImageButton

        leftImageButton.id = R.id.frame_toolbar_right_view

        if (bottomLine == null) {
            rootView.addView(leftImageButton)
        } else {
            rootView.addView(leftImageButton, rootView.childCount - 1)
        }

        val constrainSet = ConstraintSet()
        constrainSet.clone(rootView)
        constrainSet.constrainWidth(R.id.frame_toolbar_right_view, 0)
        constrainSet.constrainHeight(R.id.frame_toolbar_right_view, ConstraintSet.MATCH_CONSTRAINT)
        constrainSet.connect(
                R.id.frame_toolbar_right_view,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                0
        )
        constrainSet.connect(
                R.id.frame_toolbar_right_view,
                ConstraintSet.RIGHT,
                ConstraintSet.PARENT_ID,
                ConstraintSet.RIGHT,
                0
        )
        constrainSet.connect(
                R.id.frame_toolbar_right_view,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM,
                0
        )
        constrainSet.setDimensionRatio(R.id.frame_toolbar_right_view, "1:1")
        constrainSet.applyTo(rootView)
        return leftImageButton
    }

    /**
     * 初始化rightTextView
     */
    fun initRightTextView(rootView: ConstraintLayout, bottomLine: View?): TextView {
        val rightTextView = View.inflate(rootView.context, R.layout.frame_toolbar_text_view, null) as TextView
        rightTextView.id = R.id.frame_toolbar_right_view
        rightTextView.setTextColor(ContextCompat.getColor(KRT.getApplicationContext(), R.color.base_toolbar_right_button_font_color))

        if (bottomLine == null) {
            rootView.addView(rightTextView)
        } else {
            rootView.addView(rightTextView, rootView.childCount - 1)
        }

        val constrainSet = ConstraintSet()
        constrainSet.clone(rootView)
        constrainSet.constrainWidth(R.id.frame_toolbar_right_view, ConstraintSet.WRAP_CONTENT)
        constrainSet.constrainHeight(R.id.frame_toolbar_right_view, ConstraintSet.MATCH_CONSTRAINT)
        constrainSet.connect(
                R.id.frame_toolbar_right_view,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                0
        )
        constrainSet.connect(
                R.id.frame_toolbar_right_view,
                ConstraintSet.RIGHT,
                ConstraintSet.PARENT_ID,
                ConstraintSet.RIGHT,
                0
        )
        constrainSet.connect(
                R.id.frame_toolbar_right_view,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM,
                0
        )
        constrainSet.applyTo(rootView)
        return rightTextView
    }

    /**
     * 初始化right2ImageButton
     */
    fun initRight2ImageButton(rootView: ConstraintLayout, bottomLine: View?, rightView: View?): ImageButton {
        val leftImageButton = View.inflate(rootView.context, R.layout.frame_toolbar_image_button, null) as ImageButton
        leftImageButton.id = R.id.frame_toolbar_right_2_view
        leftImageButton.setBackgroundResource(R.drawable.base_transparent_click_bg)

        if (bottomLine == null) {
            rootView.addView(leftImageButton)
        } else {
            rootView.addView(leftImageButton, rootView.childCount - 1)
        }

        val constrainSet = ConstraintSet()
        constrainSet.clone(rootView)
        constrainSet.constrainWidth(R.id.frame_toolbar_right_2_view, 0)
        constrainSet.constrainHeight(R.id.frame_toolbar_right_2_view, ConstraintSet.MATCH_CONSTRAINT)
        constrainSet.connect(
                R.id.frame_toolbar_right_2_view,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                0
        )

        if (null == rightView) {
            constrainSet.connect(
                    R.id.frame_toolbar_right_2_view,
                    ConstraintSet.RIGHT,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.RIGHT,
                    0
            )
        } else {
            constrainSet.connect(
                    R.id.frame_toolbar_right_2_view,
                    ConstraintSet.RIGHT,
                    rightView.id,
                    ConstraintSet.LEFT,
                    0
            )
        }

        constrainSet.connect(
                R.id.frame_toolbar_right_2_view,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM,
                0
        )
        constrainSet.setDimensionRatio(R.id.frame_toolbar_right_2_view, "1:1")
        constrainSet.applyTo(rootView)
        return leftImageButton
    }

    /**
     * 初始化right2TextView
     */
    fun initRight2TextView(rootView: ConstraintLayout, bottomLine: View?, rightView: View?): TextView {
        val right2TextView = View.inflate(rootView.context, R.layout.frame_toolbar_text_view, null) as TextView
        right2TextView.id = R.id.frame_toolbar_right_2_view
        right2TextView.setTextColor(ContextCompat.getColor(KRT.getApplicationContext(), R.color.base_toolbar_right_button_font_color))

        if (bottomLine == null) {
            rootView.addView(right2TextView)
        } else {
            rootView.addView(right2TextView, rootView.childCount - 1)
        }

        val constrainSet = ConstraintSet()
        constrainSet.clone(rootView)
        constrainSet.constrainWidth(R.id.frame_toolbar_right_2_view, ConstraintSet.WRAP_CONTENT)
        constrainSet.constrainHeight(R.id.frame_toolbar_right_2_view, ConstraintSet.MATCH_CONSTRAINT)
        constrainSet.connect(
                R.id.frame_toolbar_right_2_view,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                0
        )

        if (null == rightView) {
            constrainSet.connect(
                    R.id.frame_toolbar_right_2_view,
                    ConstraintSet.RIGHT,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.RIGHT,
                    0
            )
        } else {
            constrainSet.connect(
                    R.id.frame_toolbar_right_2_view,
                    ConstraintSet.RIGHT,
                    rightView.id,
                    ConstraintSet.LEFT,
                    0
            )
        }

        constrainSet.connect(
                R.id.frame_toolbar_right_2_view,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM,
                0
        )
        constrainSet.applyTo(rootView)
        return right2TextView
    }

}