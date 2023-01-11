package com.foundation.widget.loading

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.contains
import androidx.core.view.forEach
import androidx.core.view.isVisible

/**
 *@Desc:
 *- loading View,虽然是个容器。再添额外的子view是无效的
 *- 它只会测量和拜访内置的子view
 *create by zhusw on 5/6/21 17:08
 */

class PageLoadingView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    FrameLayout(context, attributeSet, defStyleAttr), IPageLoading {

    private val ANIM_DURATION = 400L

    var verticalOffset: Int = 0
        set(value) {
            field = value
            requestLayout()
        }

    /**
     * 关闭预览模式下的效果
     */
    private var closeEffectInEditMode = true

    private var loadingState = LoadingState(isLoading = false, showBottomPlate = true)

    init {
        if (isInEditMode) {
            if (null != attributeSet) {
                val typeArray =
                    context.obtainStyledAttributes(attributeSet, R.styleable.PageLoadingView)
                closeEffectInEditMode =
                    typeArray.getBoolean(R.styleable.PageLoadingView_closeEffect, true)
                typeArray.recycle()
            }
        }
        setOnClickListener {
            //避免点击到内容页面的View
        }
    }

    private var _adapter: PageLoadingAdapter? = null
    private val adapter: PageLoadingAdapter
        get() {
            _adapter?.let {
                return it
            }
            return NormalLoadingAdapter().also {
                setLoadingAdapter(it)
            }
        }

    override var failViewEventListener: ((view: View, type: Int, extra: Any?) -> Unit)? = null

    override val isLoading
        get() = loadingState.isLoading

    private val loadingDelayedRunnable = object : Runnable {
        var showBottomPlate = true
        override fun run() {
            showLoading(showBottomPlate)
        }
    }

    override fun setLoadingAdapter(loadingAdapter: PageLoadingAdapter) {
        _adapter = loadingAdapter
        loadingAdapter.attachToParent(this)

        loadingState.isLoading = false
        removeAllViews()

        addView(adapter.singleLoadingView)

        //就算先添加 BottomPlateView 偶尔也会出现遮挡loading 主动修正一下
        adapter.singleBottomPlateView.elevation = -2F
        addView(adapter.singleBottomPlateView)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        loadingState.detachState?.let {
            if (it.isLoading) {
                showLoading(it.showBottomPlate)
            }
        }
    }

    override fun onDetachedFromWindow() {
        if (loadingState.isLoading) {
            val state = loadingState.copy()
            calDismissLoading()
            loadingState.detachState = state
        }
        animation?.cancel()
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            view?.animation?.cancel()
        }
        super.onDetachedFromWindow()
    }

    override fun showEmptyView(showBottomPlate: Boolean) {
        adapter.singleEmptyView.elevation = -1F
        addViewAndGone(adapter.singleEmptyView)

        calDismissLoading()
        removeCallbacks(loadingDelayedRunnable)
        adapter.singleLoadingView.animation?.cancel()
        adapter.singleLoadingView.animate()
            .alpha(0F)
            .setDuration(ANIM_DURATION)
            .withEndAction {
                alpha = 1F
                this@PageLoadingView.isVisible = true
                visibleViewAndGoneOther(adapter.singleEmptyView, showBottomPlate)
                adapter.onShowEmptyView()
            }
            .start()
    }

    private fun addViewAndGone(view: View) {
        if (!this.contains(view)) {
            view.isVisible = false
            addView(view)
        }
    }

    private fun visibleViewAndGoneOther(visibleView: View, showBottomPlate: Boolean) {
        this.forEach {
            when (it) {
                visibleView -> {
                    it.isVisible = true
                }
                adapter.singleBottomPlateView -> {
                    it.isVisible = showBottomPlate
                }
                else -> {
                    it.isVisible = false
                }
            }
        }
    }

    /**
     * 将提供一个透明过渡动画
     * 在动画结束后回调[PageLoadingAdapter.onShowLoading]
     * 收到回调后最好立即开始执行动画，此时loadingView一定是可见的
     */
    override fun showLoading(showBottomPlate: Boolean) {
        "showLoading showBottomPlate=$showBottomPlate".log()

        calDismissLoading()
        loadingState.isLoading = true
        loadingState.showBottomPlate = showBottomPlate

        removeCallbacks(loadingDelayedRunnable)
        alpha = 1F
        isVisible = true
        visibleViewAndGoneOther(adapter.singleLoadingView, showBottomPlate)
        adapter.singleLoadingView.run { if (alpha != 1F) alpha = 1F }
        adapter.onShowLoading()
    }

    override fun showLoadingDelayed(delayedMills: Long, showBottomPlate: Boolean) {
        removeCallbacks(loadingDelayedRunnable)
        loadingDelayedRunnable.showBottomPlate = showBottomPlate
        postDelayed(loadingDelayedRunnable, delayedMills)
    }

    override fun showLoadingFail(showBottomPlate: Boolean, type: Int, extra: Any?) {
        addViewAndGone(adapter.singleLoadingFailView)

        calDismissLoading()
        removeCallbacks(loadingDelayedRunnable)
        adapter.singleLoadingView.animation?.cancel()
        adapter.singleLoadingView.animate()
            .alpha(0F)
            .setDuration(ANIM_DURATION)
            .withEndAction {
                alpha = 1F
                visibility = View.VISIBLE
                visibleViewAndGoneOther(adapter.singleLoadingFailView, showBottomPlate)
                adapter.onShowFail(
                    type,
                    extra,
                    failViewEventListener
                )
            }
            .start()
    }

    private fun calDismissLoading() {
        loadingState.detachState = null
        if (loadingState.isLoading) {
            loadingState.isLoading = false
            adapter.onDismissLoading()
        }
    }

    /**
     * 整体停止并隐藏
     */
    override fun stop() {
        removeCallbacks(loadingDelayedRunnable)
        adapter.singleLoadingView.animation?.cancel()
        animate()
            .alpha(0F)
            .setDuration(ANIM_DURATION)
            .withEndAction {
                visibility = GONE
                calDismissLoading()
            }
            .start()
    }

    override fun asLoading(): IPageLoading = this

    /**
     * 立即显示当前view状态，用于检查停止后的view 状态
     * 是否符合预期
     * 这是一个后门方法，通常不应该使用
     */
    fun checkLoadingState() {
        alpha = 1F
        visibility = View.VISIBLE
    }


    private data class LoadingState(var isLoading: Boolean, var showBottomPlate: Boolean) {
        /**
         * detach是不是在loading中，如果是true，则attach时会再次show
         */
        var detachState: LoadingState? = null
    }
}
