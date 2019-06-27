package com.krt.frame.netmonitor.none

import com.krt.frame.netmonitor.ConnectivityMonitor

/**
 * A no-op [com.bumptech.glide.manager.ConnectivityMonitor].
 */
internal class NullConnectivityMonitor : ConnectivityMonitor {

    override fun onStart() {
        // Do nothing.
    }

    override fun onStop() {
        // Do nothing.
    }

    override fun onDestroy() {
        // Do nothing.
    }
}
