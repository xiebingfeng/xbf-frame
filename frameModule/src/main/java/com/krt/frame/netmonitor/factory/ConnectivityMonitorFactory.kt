package com.krt.frame.netmonitor.factory

import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.util.Log
import com.krt.frame.netmonitor.ConnectivityMonitor
import com.krt.frame.netmonitor.DefaultConnectivityMonitor
import com.krt.frame.netmonitor.none.NullConnectivityMonitor

class ConnectivityMonitorFactory {

    fun build(context: Context, listener: ConnectivityMonitor.ConnectivityListener): ConnectivityMonitor {
        val permissionResult = ContextCompat.checkSelfPermission(context, NETWORK_PERMISSION)
        val hasPermission = permissionResult == PackageManager.PERMISSION_GRANTED
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(
                    TAG,
                    if (hasPermission)
                        "ACCESS_NETWORK_STATE permission granted, registering connectivity monitor"
                    else
                        "ACCESS_NETWORK_STATE permission missing, cannot register connectivity monitor"
            )
        }

        return if (hasPermission)
            DefaultConnectivityMonitor(context, listener)
        else
            NullConnectivityMonitor()
    }

    companion object {
        private val TAG = "ConnectivityMonitor"
        private val NETWORK_PERMISSION = "android.permission.ACCESS_NETWORK_STATE"
    }

}
