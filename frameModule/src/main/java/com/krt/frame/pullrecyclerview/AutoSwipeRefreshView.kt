package com.krt.frame.pullrecyclerview

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet

class AutoSwipeRefreshView(
        context: Context, attrs: AttributeSet? = null
) : SwipeRefreshLayout(context, attrs) {

    var action: (() -> Unit)? = null

    fun refresh() {
        isRefreshing = true
        action?.invoke()
    }

}