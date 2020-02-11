package com.leo.treviexam.extension

import android.content.Context



fun Context.toPx(dp: Int): Int {
    val density = resources.displayMetrics.density
    return (dp.toFloat() * density).toInt()
}