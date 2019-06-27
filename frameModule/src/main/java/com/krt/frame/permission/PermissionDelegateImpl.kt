package com.krt.frame.permission

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.support.v4.app.SupportActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.krt.frame.R
import com.tbruyelle.rxpermissions2.RxPermissions

internal class PermissionDelegateImpl(private val supportActivity: SupportActivity) : PermissionDelegate {


    private val mRxPermission: RxPermissions = RxPermissions(supportActivity)

    /**
     * 电话权限
     */
    override fun checkCallPhonePermission(actionSuccess: () -> Unit?) {
        checkPermission(Manifest.permission.CALL_PHONE, actionSuccess = actionSuccess)
    }

    /**
     * 短信权限
     */
    override fun checkSendSMSPermission(actionSuccess: () -> Unit?) {
        checkPermission(Manifest.permission.SEND_SMS, actionSuccess = actionSuccess)
    }


    /**
     * 读取联系人权限
     */
    override fun checkReadContactsPermission(actionSuccess: () -> Unit?) {
        checkPermission(Manifest.permission.READ_CONTACTS, actionSuccess = actionSuccess)
    }

    /**
     * 存储权限
     */
    override fun checkStoragePermission(actionSuccess: () -> Unit?) {
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, actionSuccess = actionSuccess)
    }

    /**
     * 摄像头权限
     */
    override fun checkCameraPermission(actionSuccess: () -> Unit?) {
        checkPermission(Manifest.permission.CAMERA, actionSuccess = actionSuccess)
    }

    @SuppressLint("CheckResult")
    override fun checkPermission(vararg permissions: String, actionSuccess: () -> Unit?) {
        mRxPermission.request(*permissions)
                .subscribe {
                    if (it) {
                        actionSuccess.invoke()
                    } else {
                        showWarnAlertDialog()
                    }
                }
    }

    /**
     * 显示Dialog 跳转到本应用权限界面，打开权限
     */
    private fun showWarnAlertDialog() {
        MaterialDialog(supportActivity)
                .title(R.string.frame_base_warn)
                .message(R.string.frame_authorized_failed)
                .negativeButton(R.string.frame_base_cancel, click = {

                })
                .positiveButton(R.string.frame_base_sure, click = {
                    val intent = Intent()
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                    intent.data = Uri.fromParts("package", supportActivity.packageName, null)
                    supportActivity.startActivity(intent)
                })
                .show()
    }

}