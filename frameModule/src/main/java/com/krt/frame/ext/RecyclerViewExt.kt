package com.krt.frame.ext

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

fun RecyclerView.refreshHeight() {
    val layoutManager = object : LinearLayoutManager(context) {
        override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
            return RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }
    layoutManager.orientation = LinearLayoutManager.VERTICAL
    this.layoutManager = layoutManager
}