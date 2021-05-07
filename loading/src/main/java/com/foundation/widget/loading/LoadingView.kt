package com.foundation.widget.loading

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.SparseArray
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.DecorContentParent
import androidx.core.util.forEach

/**
 *@Desc:
 *- loading
 *-
 *create by zhusw on 5/6/21 17:08
 */
private const val ANIM_DURATION = 400L

class LoadingViewGroup(context: Context, attributeSet: AttributeSet?) :
    ViewGroup(context, attributeSet) {
    constructor(context: Context) : this(context, null)

    var failViewClickListener: (view: View) -> Unit = {}
    var loadingAdapter: LoadingAdapter = NormalLoadingAdapter()
        set(value) {
            println("LoadingViewGroup loadingAdapter set value")
            field = value
            postOnAnimation {
                resetLayout()
            }
        }

    private val undergroundImg = AppCompatImageView(context).apply {
        scaleType = ImageView.ScaleType.CENTER_CROP
        layoutParams = LayoutParams(MATCH_PART, MATCH_PART)
        addView(this)
    }
    private var loadingView: View = ImageView(context).apply {
        layoutParams = LayoutParams(34.dp, 34.dp)
        setBackgroundResource(R.drawable.loading_ic_baseline_hourglass_top_48)
        addView(this)
    }
    private var loadingFailView: View = Button(context).apply {
        text = "加载失败"
        gravity = Gravity.CENTER
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        addView(this)
        visibility = View.GONE
        setOnClickListener {
            failViewClickListener.invoke(it)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        undergroundImg.autoMeasure(this)
        loadingView.autoMeasure(this)
        loadingFailView.autoMeasure(this)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        undergroundImg.autoLayoutToCenter(this)
        loadingView.autoLayoutToCenter(this)
        loadingFailView.autoLayoutToCenter(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animation?.cancel()
        for (i in 0..childCount) {
            val view = getChildAt(i)
            view.animation?.cancel()
        }
        loadingAdapter.onStopLoading(loadingView)
    }

    private fun resetLayout() {
        loadingAdapter.run {
            getLoadingView()?.let {
                removeView(loadingView)
                loadingView = it
                loadingView.apply {
                    layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                    addView(this)
                }
            }
            getLoadingFailView()?.let {
                removeView(loadingFailView)
                loadingFailView = it
                loadingFailView.apply {
                    layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                    visibility = View.INVISIBLE
                    addView(this)
                }
            }
            if (!hideBackgroundImg()) {
                undergroundImg.visibility = View.VISIBLE
                getLoadingBackground()?.let {
                    undergroundImg.background = it
                }
            }
            getLoadingFailEventView()?.let {
                setOnClickListener { v ->
                    failViewClickListener.invoke(v)
                }
            }
        }

    }

    /**
     * 整体显示动画 与 loading 动画一起触发，避免间隔
     * 所以在onShowLoading中最好立即开始执行loading动画
     */
    fun showLoading() {
        alpha = 0F
        visibility = View.VISIBLE
        with(loadingFailView) {
            if (visibility == View.VISIBLE) visibility = View.GONE
        }
        with(loadingView) {
            if (visibility != View.VISIBLE) visibility = View.VISIBLE
            if (alpha != 1F) alpha = 1F
        }
        if (!loadingAdapter.hideBackgroundImg()) {
            with(undergroundImg) {
                if (visibility != View.VISIBLE) visibility = View.VISIBLE
            }
        }
        loadingAdapter.onShowLoading(loadingView)
        animate().alpha(1F).setDuration(ANIM_DURATION)
            .start()
    }

    fun showLoadingFail(hideBackground: Boolean) {
        loadingView.animation?.cancel()
        loadingView.animate()
            .alpha(0F)
            .setDuration(ANIM_DURATION)
            .withEndAction {
                alpha = 1F
                visibility = View.VISIBLE
                if (hideBackground) {
                    undergroundImg.run {
                        if (visibility == View.VISIBLE) visibility = View.GONE
                    }
                }
                loadingView.run {
                    if (visibility == View.VISIBLE) visibility = View.GONE
                    alpha = 1F
                }
                loadingFailView.run {
                    if (visibility != View.VISIBLE) visibility = View.VISIBLE
                }
            }
            .start()
    }

    /**
     * 整个容器隐藏后回调停止loading
     */
    fun stopLoading() {
        animate().alpha(0F).setDuration(ANIM_DURATION)
            .withEndAction {
                visibility = View.INVISIBLE
                loadingAdapter.onStopLoading(loadingView)
            }
            .start()
    }

    fun showLoadingState(){
        alpha = 1F
        visibility = View.VISIBLE
    }
}
