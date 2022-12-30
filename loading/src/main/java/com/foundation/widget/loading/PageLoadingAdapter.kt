package com.foundation.widget.loading

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible

/**
 * loading 适配器
 * 除了提供属性拓展，也提供了生命周期回调，用于状态初始化与资源释放
 * loadingView,failView,bottomPlateView,内置的 LayoutParams 都是wrap_content的
 * 如果其中一个使用了match_parent时，根布局最好是match_parent，或者固定尺寸
 * 他们都将影响根布局的测量，如同在xml设置属性一致。
 * create by zhusw on 5/7/21 09:37
 */
abstract class PageLoadingAdapter {
    private var _parentLoading: IPageLoading? = null
    private var _parentView: ViewGroup? = null
    val parentView: ViewGroup
        get() = _parentView ?: throw IllegalStateException("parentView必须在setAdapter之后才能调用")

    val context: Context get() = parentView.context

    private var _singleBottomPlateView: View? = null
    val singleBottomPlateView: View
        get() {
            _singleBottomPlateView?.let {
                return it
            }
            val v = getBottomPlateView()
                ?: View(context).apply {
                    layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    background = ColorDrawable(GlobalLoadingConfig.onInitForegroundColor)
                    elevation = singleLoadingView.elevation - 0.1F
                    isVisible = true
                }
            return v.also {
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
    fun <T> attachToParent(parent: T) where T : ViewGroup, T : IPageLoading {
        _parentView = parent
        _parentLoading = parent
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
    protected abstract fun getBottomPlateView(): View?
    protected abstract fun getLoadingView(): View
    protected abstract fun getLoadingFailView(): View
    protected abstract fun getEmptyView(): View

    abstract fun onShowLoading()

    /**
     * @param failView
     * @param type 展示失败view时的类型
     * @param extra  展示失败view时的额外参数
     * @param failViewEvent 事件响应
     */
    abstract fun onShowFail(
        type: Int = 0,
        extra: Any?,
        failViewEvent: ((view: View, type: Int, extra: Any?) -> Unit)?
    )

    abstract fun onShowEmptyView()

    /**
     * 为什么是可空类型：loading 与 fail 是完全隔离的状态，不会同时出现
     * 但他们却存在交替出现的可能，可以交替出现多次，但最终只需要停止一次
     */
    abstract fun onDismissLoading()


}