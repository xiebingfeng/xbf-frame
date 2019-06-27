package com.krt.frame.permission

import android.support.v4.app.SupportActivity

interface PermissionDelegate {

    fun checkCallPhonePermission(actionSuccess: () -> Unit?)

    fun checkSendSMSPermission(actionSuccess: () -> Unit?)

    fun checkReadContactsPermission(actionSuccess: () -> Unit?)

    fun checkStoragePermission(actionSuccess: () -> Unit?)

    fun checkCameraPermission(actionSuccess: () -> Unit?)

    fun checkPermission(vararg permissions: String, actionSuccess: () -> Unit?)

    class Factory {
        fun create(supportActivity: SupportActivity): PermissionDelegate {
            return PermissionDelegateImpl(supportActivity)
        }
    }
}
