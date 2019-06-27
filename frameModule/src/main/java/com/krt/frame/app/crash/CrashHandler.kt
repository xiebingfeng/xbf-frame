package com.krt.frame.app.crash

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.krt.frame.ext.logE

/**
 * @author xbf
 */
object CrashHandler {

    fun init(context: Context) {

        Thread.setDefaultUncaughtExceptionHandler { thread, th ->
            val sb = StringBuilder()
            sb.append("型号:" + Build.MODEL + "   系统版本:" + Build.VERSION.RELEASE)
                    .append("   versionCode:").append(getVersionCode(context))
                    .append("   versionName:").append(getVersionName(context)).append("    $th").append("\n")
            // 输出崩溃日志
            logE(sb.toString())
            System.exit(1)
            throw RuntimeException(th)
        }
    }

    private fun getVersionCode(context: Context): Int {
        try {
            val pi = context.packageManager.getPackageInfo(
                    context.packageName, 0
            )
            return pi.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
        }

        return -1
    }

    private fun getVersionName(context: Context): String? {
        try {
            val pi = context.packageManager.getPackageInfo(
                    context.packageName, 0
            )
            return pi.versionName
        } catch (e: PackageManager.NameNotFoundException) {
        }

        return null
    }

}
