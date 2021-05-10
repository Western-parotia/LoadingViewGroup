package com.foundation.widget.loading
import android.graphics.drawable.Drawable
import android.view.View

/**
 *@Desc:
 *
 * loading 适配器
 * 除了提供属性拓展，也提供了生命周期回调，用于状态初始化与资源释放
 * create by zhusw on 5/7/21 09:37
 */
interface PageLoadingAdapter {
    fun showBackgroundImg(): Boolean
    fun getBackground(): Drawable?
    fun getLoadingView(): View?
    fun getLoadingFailView(): View?
    fun onShowLoading(loadingView: View)

    /**
     * @param failView
     * @param type 展示失败view时的类型
     * @param extra 展示是被view时的额外参数
     */
    fun onShowFail(failView: View, type: Int = 0, extra: Any?)

    /**
     * 为什么是可空类型：loading 与 fail 是完全隔离的状态，不会同时出现
     * 但他们却存在交替出现的可能，可以交替出现多次，但最终只需要停止一次
     */
    fun onStop(loadingView: View?, failView: View?)

}