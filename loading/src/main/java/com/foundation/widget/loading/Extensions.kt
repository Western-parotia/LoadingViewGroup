package com.foundation.widget.loading

import android.content.res.Resources
import android.view.ViewGroup

/**
 *@Desc:
 *-
 *-
 *create by zhusw on 5/6/21 18:08
 */
private const val TAG = "BaseLoadingWidget"
internal fun String.log(secTAG: String = "") {
    if (BuildConfig.DEBUG) {
        println("$TAG : $secTAG $this")
    }
}

internal const val MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT
internal const val WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT
internal val Float.dp get() = this * Resources.getSystem().displayMetrics.density + 0.5F
internal val Int.dp get() = this.toFloat().dp.toInt()
