package com.foundation.widget.loading

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * 可作为根容器的viewGroup
 * 主动或自动装载一个loading view 在最上层
 * 如果在xml 主动加入了loading view，将不会再创建新的目标
 *create by zhusw on 5/7/21 15:08
 */
class LoadingConstraintLayout : ConstraintLayout, IPageLoading {
    private var _loadingView: PageLoadingView? = null
    private val loadingView get() = _loadingView!!

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        if (isInEditMode) {
            if (null != attributeSet) {
                val typeArray =
                    context.obtainStyledAttributes(attributeSet, R.styleable.LoadingView)
                closeEffectInEditMode =
                    typeArray.getBoolean(R.styleable.LoadingView_closeEffect, false)
            }
        }
    }

    private var closeEffectInEditMode = false

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (!isInEditMode && !closeEffectInEditMode) {
            init()
        }
    }

    private fun init() {
        //检查xml中的loading view
        for (i in 0..childCount) {
            val view = getChildAt(i)
            if (view is PageLoadingView) {
                _loadingView = view
                return
            }
        }
        if (null == _loadingView) {
            //将loadingView 加入布局顶部
            val clp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            _loadingView = PageLoadingView(context)
            _loadingView!!.let {
                it.elevation = 2F.dp
                addView(it, clp)
            }
        }
    }

    override fun asLoading(): IPageLoading = this
    override var failViewEventListener: (view: View, type: Int, extra: Any?) -> Unit
        get() = loadingView.failViewEventListener
        set(value) {
            loadingView.failViewEventListener = value
        }

    override fun setLoadingAdapter(loadingAdapter: PageLoadingAdapter) {
        loadingView.setLoadingAdapter(loadingAdapter)
    }

    override fun showLoading(showBottomPlate: Boolean) {
        loadingView.showLoading(showBottomPlate)
    }

    override fun showLoadingFail(showBottomPlate: Boolean, type: Int, extra: Any?) {
        loadingView.showLoadingFail(showBottomPlate, type, extra)
    }

    override fun showEmptyView() {
        loadingView.showEmptyView()
    }

    override fun stop() {
        loadingView.stop()
    }

    fun checkLoadingState() {
        loadingView.checkLoadingState()
    }
}
