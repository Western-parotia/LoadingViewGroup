package com.foundation.widget.loading

import android.view.View

/**
 *@Desc:
 * kotlin中，接口中定义的默认值 在实现类中无法更改
 * 接口中没有定义默认值，那么实现类也不可以定义默认值
 * tips： @JvmOverloads 无法很好的在接口场景中应用，在实现类中声明编译时会被忽略
 * create by zhusw on 5/10/21 14:02
 */
interface IPageLoading {
    var failViewEventListener: (view: View, type: Int, extra: Any?) -> Unit
    fun setLoadingAdapter(loadingAdapter: PageLoadingAdapter)
    fun showLoading(showBottomPlate: Boolean = true)

    /**
     * 延迟show
     */
    fun showLoadingDelayed(delayedMills: Long = 400L, showBottomPlate: Boolean = true)

    /**
     * type 与 extra 将在failViewEventListener中进行回传
     * 便于处理不同失败状态的响应事件
     */
    fun showLoadingFail(showBottomPlate: Boolean = true, type: Int = 0, extra: Any? = null)

    fun showEmptyView()
    fun stop()
    fun asLoading(): IPageLoading
}