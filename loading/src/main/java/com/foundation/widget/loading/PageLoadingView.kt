package com.foundation.widget.loading

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView

/**
 *@Desc:
 *- loading View,虽然是个容器。但是不可以再添加子view
 *create by zhusw on 5/6/21 17:08
 */
private const val ANIM_DURATION = 400L

class PageLoadingView(context: Context, attributeSet: AttributeSet?) :
    ViewGroup(context, attributeSet) {
    constructor(context: Context) : this(context, null)

    var failViewClickListener: (view: View) -> Unit = {}
    var loadingAdapter: PageLoadingAdapter = NormalLoadingAdapter(context)
        set(value) {
            field = value
            resetLayout()
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
    private var failView: View = Button(context).apply {
        text = "点击重试"
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
        failView.autoMeasure(this)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        undergroundImg.autoLayoutToCenter(this)
        loadingView.autoLayoutToCenter(this)
        failView.autoLayoutToCenter(this)
    }

    override fun onDetachedFromWindow() {
        "onDetachedFromWindow hashcode = ${hashCode()}".log("PageLoadingView")
        loadingAdapter.onStop(loadingView, failView)
        animation?.cancel()
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            view?.animation?.cancel()
        }
        super.onDetachedFromWindow()
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
                removeView(failView)
                failView = it
                failView.apply {
                    layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                    visibility = View.INVISIBLE
                    addView(this)
                }
            }
            if (showBackgroundImg()) {
                undergroundImg.visibility = View.VISIBLE
                getBackground()?.let {
                    undergroundImg.background = it
                }
            }
        }

    }

    /**
     * 将提供一个透明过渡动画
     * 在动画结束后回调[PageLoadingAdapter.onShowLoading]
     * 收到回调后最好立即开始执行动画，此时loadingView一定是可见的
     */
    fun showLoading() {
        alpha = 0F
        visibility = View.VISIBLE
        failView.run {
            if (visibility == View.VISIBLE) visibility = View.GONE
        }
        loadingView.run {
            if (visibility != View.VISIBLE) visibility = View.VISIBLE
            if (alpha != 1F) alpha = 1F
        }

        if (loadingAdapter.showBackgroundImg()) {
            undergroundImg.run {
                if (visibility != View.VISIBLE) visibility = View.VISIBLE
            }
        }
        animate()
            .alpha(1F)
            .setDuration(ANIM_DURATION)
            .withEndAction {
                loadingAdapter.onShowLoading(loadingView)
            }
            .start()
    }

    @JvmOverloads
    fun showLoadingFail(hideBackground: Boolean = true, type: Int = 0, extra: Any? = null) {
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
                }
                failView.run {
                    if (visibility != View.VISIBLE) visibility = View.VISIBLE
                }
                loadingAdapter.onShowFail(failView, type, extra)
            }
            .start()
    }

    /**
     * 整体停止并隐藏
     */
    fun stop() {
        animate()
            .alpha(0F)
            .setDuration(ANIM_DURATION)
            .withEndAction {
                visibility = View.GONE
                loadingAdapter.onStop(loadingView, failView)
            }
            .start()
    }

    /**
     * 立即显示当前view状态，用于检查停止后的view 状态
     * 是否符合预期
     * 这是一个后门方法，通常不应该使用
     */
    fun checkLoadingState() {
        alpha = 1F
        visibility = View.VISIBLE
    }
}
