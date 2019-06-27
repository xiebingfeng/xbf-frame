package com.krt.frame.frame.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.AnimationSet
import android.widget.TextView
import com.krt.frame.R
import com.krt.frame.frame.dialog.utils.OptAnimationLoader

class LoadingDialog(context: Context, val prompt: String? = null, cancelable: Boolean? = true) :
        Dialog(context, R.style.frame_loading_dialog) {

    private var mDialogView: View? = null

    private val mModalInAnim: AnimationSet
    private val mModalOutAnim: AnimationSet

    private var mCountDownTimer: CountDownTimer? = null

    init {
        setCanceledOnTouchOutside(false)

        if (cancelable != null) {
            setCancelable(cancelable)
        }

        mModalInAnim = OptAnimationLoader.loadAnimation(context, R.anim.frame_loading_dialog_in) as AnimationSet
        mModalOutAnim = OptAnimationLoader.loadAnimation(context, R.anim.frame_loading_dialog_out) as AnimationSet
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.frame_dialog_loading)
        mDialogView = window!!.decorView.findViewById(android.R.id.content)

        prompt?.apply {
            findViewById<TextView>(R.id.tv_prompt).text = this
        }

        var i = -1

        mCountDownTimer = object : CountDownTimer((800 * 111117).toLong(), 800) {
            override fun onTick(millisUntilFinished: Long) {
                i++
//                when (i) {
//                    0 -> progress_wheel.barColor = ContextCompat.getColor(context, R.color.dev_loading_view_color1)
//                    1 -> progress_wheel.barColor = ContextCompat.getColor(context, R.color.dev_loading_view_color2)
//                    2 -> progress_wheel.barColor = ContextCompat.getColor(context, R.color.dev_loading_view_color3)
//                    3 -> progress_wheel.barColor = ContextCompat.getColor(context, R.color.dev_loading_view_color4)
//                    4 -> progress_wheel.barColor = ContextCompat.getColor(context, R.color.dev_loading_view_color5)
//                    5 -> {
//                        progress_wheel.barColor = ContextCompat.getColor(context, R.color.dev_loading_view_color6)
//                        i = 0
//                    }
//                }
            }

            override fun onFinish() {
                i = -1
            }
        }.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        //防止内存泄漏
        mCountDownTimer?.cancel()
    }

    override fun onStart() {
        super.onStart()
        mDialogView?.startAnimation(mModalInAnim)
    }
}