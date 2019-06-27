package com.krt.frame.app

import com.alibaba.android.arouter.launcher.ARouter


/**
 * 初始化某个Module
 */
fun initModule(moduleApp: String): IModuleService? {
    ARouter.getInstance().build(moduleApp).navigation()?.let {
        return (it as BaseApp).initModuleApp()
    }
    return null
}