package com.krt.frame.pullrecyclerview

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.krt.frame.R
import com.krt.frame.app.config.KRT

class ColorDividerItemDecoration : RecyclerView.ItemDecoration() {

    private var mDividerHeight: Float = 0.toFloat()

    private val mPaint: Paint = Paint()

    init {
        mPaint.isAntiAlias = true
        mPaint.color = ContextCompat.getColor(KRT.getApplicationContext(), R.color.base_list_view_divider_item_color)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        //        //第一个ItemView不需要在上面绘制分割线
        if (parent.getChildAdapterPosition(view) != 0) {
            //这里直接硬编码为1px
            val dimen = KRT.getApplicationContext().resources.getDimension(R.dimen.base_1)

            outRect.top = dimen.toInt()
            mDividerHeight = dimen
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val childCount = parent.childCount

        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)

            val index = parent.getChildAdapterPosition(view)
            //第一个ItemView不需要绘制
            if (index == 0) {
                continue
            }

            val dividerTop = view.top - mDividerHeight
            val dividerLeft = parent.paddingLeft.toFloat()
            val dividerBottom = view.top.toFloat()
            val dividerRight = (parent.width - parent.paddingRight).toFloat()

            c.drawRect(dividerLeft, dividerTop, dividerRight, dividerBottom, mPaint)
        }
    }
}