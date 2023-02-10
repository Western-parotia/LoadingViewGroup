package com.foundation.widget.loading

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import java.util.*

/**
 * loading 适配器
 * 除了提供属性拓展，也提供了生命周期回调，用于状态初始化与资源释放
 * loadingView,failView,bottomPlateView,内置的 LayoutParams 都是wrap_content的
 * 如果其中一个使用了match_parent时，根布局最好是match_parent，或者固定尺寸
 * 他们都将影响根布局的测量，如同在xml设置属性一致。
 * create by zhusw on 5/7/21 09:37
 */
abstract class PageLoadingAdapter {
    private var lazyAttachListener: LinkedList<() -> Unit>? = null

    private var _parentLoading: IPageLoading? = null
    private var _parentView: ViewGroup? = null
    val parentView: ViewGroup
        get() = _parentView
            ?: throw IllegalStateException("parentView必须在setAdapter之后才能调用，或者使用lazyOnAttached方法")

    /**
     * 注意调用时机，只能在setAdapter之后。[getBottomPlateView]等可以直接使用
     * 见[lazyOnAttached]，可延迟调用
     */
    val attachContext: Context get() = parentView.context

    /***
     * 注意调用时机
     * 见[lazyOnAttached]，可延迟调用
     */
    private var _singleBottomPlateView: View? = null
    val singleBottomPlateView: View
        get() {
            _singleBottomPlateView?.let {
                return it
            }
            return getBottomPlateView().also {
                addDefLayoutParams(it)
                _singleBottomPlateView = it
            }
        }

    private var _singleLoadingView: View? = null
    val singleLoadingView: View
        get() {
            _singleLoadingView?.let {
                return it
            }
            return getLoadingView().also {
                addDefLayoutParams(it)
                _singleLoadingView = it
            }
        }

    private var _singleLoadingFailView: View? = null
    val singleLoadingFailView: View
        get() {
            _singleLoadingFailView?.let {
                return it
            }
            return getLoadingFailView().also {
                addDefLayoutParams(it)
                _singleLoadingFailView = it
            }
        }

    private var _singleEmptyView: View? = null
    val singleEmptyView: View
        get() {
            _singleEmptyView?.let {
                return it
            }
            return getEmptyView().also {
                addDefLayoutParams(it)
                _singleEmptyView = it
            }
        }

    private fun addDefLayoutParams(v: View) {
        when (val params = v.layoutParams) {
            null -> {
                v.layoutParams = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                    .apply { gravity = Gravity.CENTER }
            }
            is FrameLayout.LayoutParams -> {
                if (params.gravity == FrameLayout.LayoutParams.UNSPECIFIED_GRAVITY) {
                    params.gravity = Gravity.CENTER
                }
                return
            }
            else -> {
                v.layoutParams = FrameLayout.LayoutParams(params)
                    .apply { gravity = Gravity.CENTER }
            }
        }
    }

    /**
     * 当被setAdapter时，会率先调用此方法
     */
    internal fun <T> attachToParent(parent: T) where T : ViewGroup, T : IPageLoading {
        //重置view
        notifyViewChanged()

        _parentView = parent
        _parentLoading = parent
        lazyAttachListener?.forEach {
            it.invoke()
        }
        lazyAttachListener = null
    }

    /**
     * 当你重新设置其中的view时调用
     * notify后会重新回到默认loading状态
     */
    fun notifyViewChanged() {
        _singleBottomPlateView = null
        _singleLoadingView = null
        _singleLoadingFailView = null
        _singleEmptyView = null
        _parentLoading?.setLoadingAdapter(this)
    }

    /**
     * 几个view获取，除了BottomPlateView其他默认居中展示（可以自行设置FrameLayout.LayoutParams）
     */
    protected open fun getBottomPlateView(): View =
        View(attachContext).apply {
            layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            setBackgroundColor(GlobalLoadingConfig.onInitForegroundColor)
            elevation = singleLoadingView.elevation - 0.1F
            isVisible = true
        }

    protected abstract fun getLoadingView(): View
    protected abstract fun getLoadingFailView(): View
    protected abstract fun getEmptyView(): View

    open fun onShowLoading() {}

    /**
     * @param type 展示失败view时的类型
     * @param extra  展示失败view时的额外参数
     * @param failViewEvent 事件响应
     */
    open fun onShowFail(
        type: Int = 0,
        extra: Any?,
        failViewEvent: ((view: View, type: Int, extra: Any?) -> Unit)?
    ) {
        if (failViewEvent != null) {
            singleLoadingFailView.setOnClickListener {
                failViewEvent.invoke(singleLoadingFailView, type, extra)
            }
        }
    }

    open fun onShowEmptyView() {}

    open fun onDismissLoading() {}

    /**
     * 如果attach了则立即调用，如果未attach则等待attach后自动调用
     */
    fun lazyOnAttached(callback: () -> Unit) {
        if (_parentView == null) {
            if (lazyAttachListener == null) {
                lazyAttachListener = LinkedList()
            }
            lazyAttachListener?.add(callback)
        } else {
            callback.invoke()
        }
    }
}