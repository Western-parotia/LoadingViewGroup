package com.foundation.widget.loading

import android.view.View


/**
 * 大多数时候都需要统一管理动画的结束
 * create by zhusw on 5/7/21 14:57
 */
open class StreamerPageLoadingAdapter(val streamerView: StreamerConstraintLayout) :
    PageLoadingAdapter {
    override fun getBottomPlateView(): View? = null
    override fun getLoadingView(): StreamerConstraintLayout? = streamerView
    override fun getLoadingFailView(): View? = null
    override fun getEmptyView(): View? = null

    /**
     * 属性动画 默认是弱引用的，不必考虑释放问题
     */
    override fun onShowLoading(loadingView: View) {
        streamerView.start()
    }

    override fun onShowFail(
        failView: View,
        type: Int,
        extra: Any?,
        failViewEvent: (view: View, type: Int, extra: Any?) -> Unit
    ) {
        failView.setOnClickListener {
            failViewEvent.invoke(failView, type, extra)
        }
    }

    override fun onShowEmptyView(emptyView: View) {
    }

    override fun onDismissLoading(loadingView: View?, failView: View?) {
        streamerView.stop()
    }

}

