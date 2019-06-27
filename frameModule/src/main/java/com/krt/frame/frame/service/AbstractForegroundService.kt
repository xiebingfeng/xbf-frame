package com.krt.frame.frame.service

import android.app.IntentService
import android.app.Notification
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock

abstract class AbstractForegroundService(name: String, private val notificationContentTitleResId: Int) :
        IntentService(name) {
    private val notificationId: Int

    init {
        notificationId = SystemClock.uptimeMillis().toInt()
    }

    override fun onCreate() {
        super.onCreate()
        showForegroundNotification(
                100, 0, true,
//            getString(R.string.leak_canary_notification_foreground_text)
                "testFore"
        )
    }

    protected fun showForegroundNotification(
            max: Int, progress: Int, indeterminate: Boolean,
            contentText: String
    ) {
        val builder = Notification.Builder(this)
                .setContentTitle(getString(notificationContentTitleResId))
                .setContentText(contentText)
                .setProgress(max, progress, indeterminate)
//        val notification = LeakCanaryInternals.buildNotification(this, builder)
//        startForeground(notificationId, notification)
    }

    override fun onHandleIntent(intent: Intent?) {
        onHandleIntentInForeground(intent)
    }

    protected abstract fun onHandleIntentInForeground(intent: Intent?)

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
