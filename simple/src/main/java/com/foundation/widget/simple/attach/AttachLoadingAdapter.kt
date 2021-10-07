package com.foundation.widget.simple.attach

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.foundation.widget.loading.NormalLoadingAdapter
import com.foundation.widget.loading.StreamerConstraintLayout
import com.foundation.widget.simple.R

private const val ANIM_DURATION_LONG = 800L

/**
 * create by zhusw on 8/2/21 14:45
 */
open class ArchLoadingAdapter(
    private val context: Context,
    private val onRetryClick: (() -> Unit)? = null
) : NormalLoadingAdapter() {

    override fun getEmptyView(): View? {
        return View(context).apply {
            setBackgroundColor(0xff999999.toInt())
            layoutParams = ViewGroup.LayoutParams(100, 100)
            setOnClickListener { onRetryClick?.invoke() }
        }
    }

    override fun getLoadingView(): View? {
        return StreamerConstraintLayout(context).apply {
            val wh = 500
            val sw = 100f //比设计稿 小点
            animDuration = 750L
            streamerWidth = sw
            streamerColor = Color.parseColor("#26000000")
            addView(ImageView(context).apply {
                layoutParams = ViewGroup.LayoutParams(wh, wh)
                setImageResource(R.drawable.img_loading_logo)
            })
        }
    }

    override fun onShowLoading(loadingView: View) {
        (loadingView as? StreamerConstraintLayout)?.start()
    }

    override fun onDismissLoading(loadingView: View?, failView: View?) {
        (loadingView as? StreamerConstraintLayout)?.stop()
    }
}