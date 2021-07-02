package com.foundation.widget.loading

import android.content.res.Resources
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup

/**
 *@Desc:
 *-
 *-
 *create by zhusw on 5/6/21 18:08
 */
private const val TAG ="BaseLoadingWidget"
internal fun String.log(secTAG: String = "") {
    if (BuildConfig.DEBUG) {
        println("$TAG : $secTAG $this")
    }
}

internal const val MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT
internal const val WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT
internal val Float.dp get() = this * Resources.getSystem().displayMetrics.density + 0.5F
internal val Int.dp get() = this.toFloat().dp.toInt()
internal val DRAWABLE_WHITE = GradientDrawable().apply {
    setColor(GlobalLoadingConfig.onInitForegroundColor)
}

internal fun Int.toExactlyMeasureSpec(): Int {
    return View.MeasureSpec.makeMeasureSpec(this, View.MeasureSpec.EXACTLY)
}

internal fun Int.toAtMostMeasureSpec(): Int {
    return View.MeasureSpec.makeMeasureSpec(this, View.MeasureSpec.AT_MOST)
}

internal fun View.defaultWidthMeasureSpec(parentView: ViewGroup): Int {
    return when (layoutParams.width) {
        MATCH_PARENT -> parentView.measuredWidth.toExactlyMeasureSpec()
        WRAP_CONTENT -> WRAP_CONTENT.toAtMostMeasureSpec()
        0 -> throw IllegalAccessException("need special treatment for $parentView width")
        else -> {
            layoutParams.width.toExactlyMeasureSpec()
        }
    }
}
internal fun View.defaultHeightMeasureSpec(parentView: ViewGroup):Int{
    return when (layoutParams.height) {
        MATCH_PARENT -> parentView.measuredHeight.toExactlyMeasureSpec()
        WRAP_CONTENT -> WRAP_CONTENT.toAtMostMeasureSpec()
        0 -> throw IllegalAccessException("need special treatment for $parentView height")
        else -> {
            layoutParams.height.toExactlyMeasureSpec()
        }
    }
}
internal fun View.autoMeasure(parentView: ViewGroup){
    measure(
        defaultWidthMeasureSpec(parentView),
        defaultHeightMeasureSpec(parentView),
    )
}

internal fun View.autoLayoutToCenter(parentView: ViewGroup){
    layout((parentView.measuredWidth  - measuredWidth) /2,
        (parentView.measuredHeight - measuredHeight) /2,
        (parentView.measuredWidth  + measuredWidth) /2,
        (parentView.measuredHeight  + measuredHeight) /2)
}