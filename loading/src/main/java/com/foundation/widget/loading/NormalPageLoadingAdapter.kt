package com.foundation.widget.loading

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import androidx.core.util.forEach


private const val ANIM_DURATION_LONG = 800L

/**
 * 大多数时候都需要统一管理动画的结束
 * create by zhusw on 5/7/21 14:57
 */
open class NormalLoadingAdapter : PageLoadingAdapter() {
    private val animCache: SparseArray<ObjectAnimator> = SparseArray()

    override fun getBottomPlateView(): View? = null

    override fun getLoadingView(): View =
        ImageView(context).apply {
            visibility = View.VISIBLE
            layoutParams = ViewGroup.LayoutParams(34.dp, 34.dp)
            setBackgroundResource(R.drawable.loading_ic_baseline_hourglass_top_48)
        }

    override fun getLoadingFailView(): View =
        TextView(context).apply {
            text = "点击重试"
            setPadding(20, 10, 20, 10)
            setBackgroundColor(0xffeeeeee.toInt())
            layoutParams = ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            visibility = View.INVISIBLE
        }

    override fun getEmptyView(): View =
        TextView(context).apply {
            layoutParams = ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            text = "无数据"
            visibility = View.INVISIBLE
            elevation = singleLoadingView.elevation - 0.2F
        }

    /**
     * 属性动画 默认是弱引用的，不必考虑释放问题
     */
    override fun onShowLoading() {
        ObjectAnimator.ofFloat(singleLoadingView, "rotationX", 0F, 200F).apply {
            repeatCount = Animation.INFINITE
            duration = ANIM_DURATION_LONG
            repeatMode = ValueAnimator.RESTART
            interpolator = AccelerateDecelerateInterpolator()
            start()
            animCache.put(animCache.size(), this)
        }
        ObjectAnimator.ofFloat(singleLoadingView, "alpha", 0.2F, 0.8F).apply {
            repeatCount = Animation.INFINITE
            repeatMode = ValueAnimator.REVERSE
            duration = ANIM_DURATION_LONG
            start()
            animCache.put(animCache.size(), this)
        }
    }

    override fun onShowFail(
        type: Int,
        extra: Any?,
        failViewEvent: ((view: View, type: Int, extra: Any?) -> Unit)?
    ) {
        if (failViewEvent != null) {
            singleLoadingFailView.setOnClickListener {
                failViewEvent.invoke(singleLoadingFailView, type, extra)
            }
        }
    }

    override fun onShowEmptyView() {

    }

    override fun onDismissLoading() {
        singleLoadingFailView.animation?.cancel()
        animCache.forEach { _, value ->
            value.cancel()
        }
        animCache.clear()
    }
}