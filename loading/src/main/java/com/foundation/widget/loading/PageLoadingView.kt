package com.foundation.widget.loading

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView

/**
 *@Desc:
 *- loading View,虽然是个容器。再添额外的子view是无效的
 *- 它只会测量和拜访内置的子view
 *create by zhusw on 5/6/21 17:08
 */
private const val ANIM_DURATION = 400L

class PageLoadingView(context: Context, attributeSet: AttributeSet?) :
    ViewGroup(context, attributeSet), IPageLoading {
    constructor(context: Context) : this(context, null)

    private var adapter: PageLoadingAdapter = NormalLoadingAdapter()

    private var bottomPlateView: View = View(context).apply {
        layoutParams = LayoutParams(1.dp, 1.dp)
        addView(this)
    }
    private var loadingView: View = ImageView(context).apply {
        layoutParams = LayoutParams(34.dp, 34.dp)
        setBackgroundResource(R.drawable.loading_ic_baseline_hourglass_top_48)
        addView(this)
    }
    private var failView: View = Button(context).apply {
        text = "点击重试"
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        addView(this)
        visibility = View.GONE
    }
    override var failViewEventListener: (view: View, type: Int, extra: Any?) -> Unit =
        { _: View, _: Int, _: Any? -> }

    override fun setLoadingAdapter(loadingAdapter: PageLoadingAdapter) {
        adapter = loadingAdapter
        resetLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        bottomPlateView.autoMeasure(this)
        loadingView.autoMeasure(this)
        failView.autoMeasure(this)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        bottomPlateView.autoLayoutToCenter(this)
        loadingView.autoLayoutToCenter(this)
        failView.autoLayoutToCenter(this)
    }

    override fun onDetachedFromWindow() {
        adapter.onStop(loadingView, failView)
        animation?.cancel()
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            view?.animation?.cancel()
        }
        super.onDetachedFromWindow()
    }

    private fun resetLayout() {
        adapter.run {
            getLoadingView()?.let {
                removeView(loadingView)
                loadingView = it
                addView(loadingView)
            }
            getBottomPlateView()?.let {
                removeView(bottomPlateView)
                bottomPlateView = it
                //就算先添加 BottomPlateView 偶尔也会出现遮挡loading 主动修正一下
                bottomPlateView.elevation = loadingView.elevation - 0.1F
                addView(bottomPlateView)
            }
            getLoadingFailView()?.let {
                removeView(failView)
                failView = it.apply {
                    visibility = View.GONE
                }
                addView(failView)
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
        alpha = 0F
        visibility = View.VISIBLE
        failView.visibility = View.GONE
        bottomPlateView.visibility = if (showBottomPlate) View.VISIBLE else View.GONE
        loadingView.run {
            visibility = View.VISIBLE
            if (alpha != 1F) alpha = 1F
        }
        animate()
            .alpha(1F)
            .setDuration(ANIM_DURATION)
            .withEndAction {
                adapter.onShowLoading(loadingView)
            }
            .start()
    }

    override fun showLoadingFail(showBottomPlate: Boolean, type: Int, extra: Any?) {
        adapter.onStop(loadingView, failView)
        loadingView.animation?.cancel()
        loadingView.animate()
            .alpha(0F)
            .setDuration(ANIM_DURATION)
            .withEndAction {
                alpha = 1F
                visibility = View.VISIBLE
                bottomPlateView.visibility = if (showBottomPlate) View.VISIBLE else View.GONE
                loadingView.visibility = View.GONE
                failView.visibility = View.VISIBLE
                adapter.onShowFail(failView, type, extra, failViewEventListener)
            }
            .start()
    }

    /**
     * 整体停止并隐藏
     */
    override fun stop() {
        animate()
            .alpha(0F)
            .setDuration(ANIM_DURATION)
            .withEndAction {
                visibility = View.GONE
                adapter.onStop(loadingView, failView)
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
}
