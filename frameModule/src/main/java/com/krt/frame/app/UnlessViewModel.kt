package com.krt.frame.app

import android.app.Application
import com.krt.frame.frame.mvvm.BaseViewModel

/**
 * 一个没有用的  ViewModel,有些Fragment要用到lce，又不需要ViewMdel
 */
class UnlessViewModel(application: Application) : BaseViewModel(application)