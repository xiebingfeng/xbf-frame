package com.krt.network.ext

import com.krt.frame.app.config.KRT

object NetWorkUtils {

    fun getString(dimen: Int): String = KRT.getApplicationContext().resources.getString(dimen)

}