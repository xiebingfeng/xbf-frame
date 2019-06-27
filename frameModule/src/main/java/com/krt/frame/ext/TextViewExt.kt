package com.krt.frame.ext

import android.graphics.Color
import android.text.*
import android.text.style.ForegroundColorSpan
import android.widget.TextView

fun TextView.toSpannableString(color: Int = Color.RED, start: Int = 0, end: Int = 1) {
    val style = SpannableStringBuilder(text)
    style.setSpan(ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)     //设置指定位置文字的颜色
    text = style
}

//fun TextView.toSpannableStringEnd(color: Int = Color.RED) {
//    val style = SpannableStringBuilder(text)
//    val textLength = this.length()
//    style.setSpan(ForegroundColorSpan(color), textLength - 1, textLength, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)     //设置指定位置文字的颜色
//    text = style
//}

fun TextView.getNetParamText(): String? {
    val textResult = this.text.toString().trim()

    if (TextUtils.isEmpty(textResult)) {
        return null
    }
    return textResult
}

fun TextView.onTextChange(function: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            function.invoke(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {
        }

    })
}
