package com.foundation.widget.loading

import android.animation.Animator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView

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

    var failViewClickListener: FailViewClickListener? = null
    var loadingAdapter: LoadingAdapter? = null
        set(value) {
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
    private var loadingView: View = TextView(context).apply {
        text = "loading"
        gravity = Gravity.CENTER
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        setBackgroundColor(Color.BLUE)
        addView(this)
    }
    private var loadingFailView: View = Button(context).apply {
        text = "加载失败"
        gravity = Gravity.CENTER
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        addView(this)
        visibility = View.INVISIBLE
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
    }

    private fun resetLayout() {
        removeAllViews()
        loadingAdapter?.run {
            getLoadingView()?.let {
                loadingView = it
                loadingView.apply {
                    layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                    addView(this)
                }
            }
            getLoadingFailView()?.let {
                loadingFailView = it
                loadingFailView.apply {
                    layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                    visibility = View.INVISIBLE
                    addView(this)
                }

            }
            getLoadingBackground()?.let {
                undergroundImg.background = it
            }
            getLoadingFailEventView()?.setOnClickListener {
                failViewClickListener?.onClick(it)
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
            if (visibility == View.VISIBLE) visibility = View.INVISIBLE
        }
        with(loadingView) {
            if (visibility != View.VISIBLE) visibility = View.VISIBLE
        }
        loadingAdapter?.onShowLoading(loadingView)
        animate().alpha(1F).setDuration(ANIM_DURATION)
            .start()
    }

    /**
     * 整个容器隐藏后回调停止loading
     */
    fun stopLoading() {
        animate().alpha(0F).setDuration(ANIM_DURATION)
            .setListener(object : AnimatorEndListener() {
                override fun onEnd(animation: Animator?) {
                    visibility = View.GONE
                    loadingAdapter?.onStopLoading(loadingView)
                }
            })
            .start()
    }


}

interface FailViewClickListener {
    fun onClick(view: View);
}

private abstract class AnimatorEndListener : Animator.AnimatorListener {
    override fun onAnimationStart(animation: Animator?) {

    }

    override fun onAnimationEnd(animation: Animator?) {
        onEnd(animation)
    }

    override fun onAnimationCancel(animation: Animator?) {
    }

    override fun onAnimationRepeat(animation: Animator?) {
    }

    abstract fun onEnd(animation: Animator?)
}

