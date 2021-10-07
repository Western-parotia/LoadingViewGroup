package com.foundation.widget.loading

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.util.SparseArray
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import androidx.core.util.forEach


private const val ANIM_DURATION_LONG = 800L

/**
 * 大多数时候都需要统一管理动画的结束
 * create by zhusw on 5/7/21 14:57
 */
open class NormalLoadingAdapter : PageLoadingAdapter {
    private val animCache: SparseArray<ObjectAnimator> = SparseArray()
    override fun getBottomPlateView(): View? = null
    override fun getLoadingView(): View? = null
    override fun getLoadingFailView(): View? = null
    override fun getEmptyView(): View? = null

    /**
     * 属性动画 默认是弱引用的，不必考虑释放问题
     */
    override fun onShowLoading(loadingView: View) {
        ObjectAnimator.ofFloat(loadingView, "rotationX", 0F, 200F).apply {
            repeatCount = Animation.INFINITE
            duration = ANIM_DURATION_LONG
            repeatMode = ValueAnimator.RESTART
            interpolator = AccelerateDecelerateInterpolator()
            start()
            animCache.put(animCache.size(), this)
        }
        ObjectAnimator.ofFloat(loadingView, "alpha", 0.2F, 0.8F).apply {
            repeatCount = Animation.INFINITE
            repeatMode = ValueAnimator.REVERSE
            duration = ANIM_DURATION_LONG
            start()
            animCache.put(animCache.size(), this)
        }
    }

    override fun onShowFail(
        failView: View,
        type: Int,
        extra: Any?,
        failViewEvent: (view: View, type: Int, extra: Any?) -> Unit
    ) {
        failView.setOnClickListener {
            failViewEvent.invoke(failView, type, extra)
        }
    }

    override fun onShowEmptyView(emptyView: View) {

    }

    override fun onDismissLoading(loadingView: View?, failView: View?) {
        loadingView?.animation?.cancel()
        animCache.forEach { _, value ->
            value.cancel()
        }
        animCache.clear()
    }
}