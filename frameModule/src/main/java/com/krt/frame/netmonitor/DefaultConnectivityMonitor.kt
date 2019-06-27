package com.krt.frame.netmonitor

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import me.jessyan.autosize.utils.Preconditions

/**
 * xbf  copy form glide
 */
class DefaultConnectivityMonitor(
        context: Context,
        val listener: ConnectivityMonitor.ConnectivityListener
) : ConnectivityMonitor {
    private val context: Context = context.applicationContext

    var isConnected: Boolean = false
    private var isRegistered: Boolean = false

    private val connectivityReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val wasConnected = isConnected
            isConnected = isConnected(context)
            if (wasConnected != isConnected) {
                if (Log.isLoggable(TAG, Log.DEBUG)) {
                    Log.d(TAG, "connectivity changed, isConnected: $isConnected")
                }
                listener.onConnectivityChanged(isConnected)
            }
        }
    }

    private fun register() {
        if (isRegistered) {
            return
        }

        // Initialize isConnected.
        isConnected = isConnected(context)
        try {
            // See #1405
            context.registerReceiver(
                    connectivityReceiver,
                    IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
            isRegistered = true
        } catch (e: SecurityException) {
            // See #1417, registering the receiver can throw SecurityException.
            if (Log.isLoggable(TAG, Log.WARN)) {
                Log.w(TAG, "Failed to register", e)
            }
        }

    }

    private fun unregister() {
        if (!isRegistered) {
            return
        }

        context.unregisterReceiver(connectivityReceiver)
        isRegistered = false
    }

    // Permissions are checked in the factory instead.
    @SuppressLint("MissingPermission")
    fun isConnected(context: Context): Boolean {
        val connectivityManager = Preconditions.checkNotNull(
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        )
        val networkInfo: NetworkInfo?
        try {
            networkInfo = connectivityManager.activeNetworkInfo
        } catch (e: RuntimeException) {
            // #1405 shows that this throws a SecurityException.
            // b/70869360 shows that this throws NullPointerException on APIs 22, 23, and 24.
            // b/70869360 also shows that this throws RuntimeException on API 24 and 25.
            if (Log.isLoggable(TAG, Log.WARN)) {
                Log.w(TAG, "Failed to determine connectivity status when connectivity changed", e)
            }
            // Default to true;
            return true
        }

        return networkInfo != null && networkInfo.isConnected
    }

    override fun onStart() {
        register()
    }

    override fun onStop() {
        unregister()
    }

    override fun onDestroy() {
        // Do nothing.
    }

    companion object {
        private val TAG = "ConnectivityMonitor"
    }
}
