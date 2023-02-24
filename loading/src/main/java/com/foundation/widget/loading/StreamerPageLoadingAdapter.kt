package com.foundation.widget.loading


/**
 * 大多数时候都需要统一管理动画的结束
 * create by zhusw on 5/7/21 14:57
 */
open class StreamerPageLoadingAdapter(val streamerView: StreamerConstraintLayout) :
    NormalLoadingAdapter() {
    override fun getLoadingView(): StreamerConstraintLayout = streamerView

    /**
     * 属性动画 默认是弱引用的，不必考虑释放问题
     */
    override fun onShowLoading() {
        streamerView.start()
    }

    override fun onDismissLoading() {
        streamerView.stop()
    }

}

