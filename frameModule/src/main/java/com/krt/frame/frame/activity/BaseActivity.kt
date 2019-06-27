package com.krt.frame.frame.activity

import android.app.ActivityManager
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import com.blankj.utilcode.util.KeyboardUtils
import com.krt.frame.app.AppManager
import com.krt.frame.frame.fragment.BaseFragment
import me.jessyan.autosize.internal.CustomAdapt
import me.yokeyword.fragmentation.SupportActivity
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator
import me.yokeyword.fragmentation.anim.FragmentAnimator


abstract class BaseActivity : SupportActivity(), CustomAdapt {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppManager.addActivity(this)

        getRootFragment()?.let {
            loadRootFragment(android.R.id.content, it)
        }
    }

    override fun onResume() {
        if (!isActive) {
            isActive = true
//            Logger.d("ACTIVITY", "程序从后台唤醒")
        }
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
        if (!isAppOnForeground()) {
            isActive = false
//            Logger.d("ACTIVITY", "程序进入后台")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AppManager.finishActivity(this)
        KeyboardUtils.fixSoftInputLeaks(this)
    }

    abstract fun getRootFragment(): BaseFragment?

    override fun isBaseOnWidth(): Boolean = true

    override fun getSizeInDp(): Float {
        return if (isPad(this)) {
            550f
        } else {
            375f
        }
    }

    private fun isPad(context: Context): Boolean {
        return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    /**
     * APP是否处于前台唤醒状态
     *
     * @return
     */
    private fun isAppOnForeground(): Boolean {
        val activityManager = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val packageName = applicationContext.packageName
        val appProcesses = activityManager
                .runningAppProcesses ?: return false

        for (appProcess in appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName == packageName && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true
            }
        }

        return false
    }

    override fun onCreateFragmentAnimator(): FragmentAnimator {
        return DefaultHorizontalAnimator()
    }


    companion object {
        var isActive: Boolean = false //全局变量
    }

}
