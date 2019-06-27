package com.krt.frame.pullrecyclerview

/**
 * Created by jianghejie on 15/11/22.
 */
internal interface BaseRefreshHeader {

    fun onMove(delta: Float)

    fun releaseAction(): Boolean

    fun refreshComplete(completeImmediately: Boolean)

    companion object {

        val STATE_NORMAL = 0
        val STATE_RELEASE_TO_REFRESH = 1
        val STATE_REFRESHING = 2  //刷新
        val STATE_DONE = 3  //完成
    }

}