package com.krt.frame.netmonitor

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.krt.frame.app.config.KRT
import com.krt.frame.netmonitor.factory.ConnectivityMonitorFactory

class NetworkChangeManager : LifecycleObserver {

    private lateinit var connectivityMonitor: ConnectivityMonitor

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        connectivityMonitor = ConnectivityMonitorFactory().build(KRT.getApplicationContext(),
                object : ConnectivityMonitor.ConnectivityListener {
                    override fun onConnectivityChanged(isConnected: Boolean) {
                    }

                })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        connectivityMonitor.onStart()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        connectivityMonitor.onStop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {

    }
}