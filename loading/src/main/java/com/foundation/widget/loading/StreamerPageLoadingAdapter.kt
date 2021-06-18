package com.foundation.widget.loading

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout


/**
 * 大多数时候都需要统一管理动画的结束
 * create by zhusw on 5/7/21 14:57
 */
open class StreamerPageLoadingAdapter(private val context: Context) : PageLoadingAdapter {

    private val streamerView = StreamerConstraintLayout(context)
    private val tv = TextView(context)

    init {
        val tv = TextView(context)
        tv.text = "努力加载中"
        tv.textSize = 20F.dp
        tv.typeface = Typeface.DEFAULT_BOLD
        tv.setTextColor(Color.parseColor("#dbdbdb"))
        val lp = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        streamerView.addView(tv, lp)
        streamerView.streamerColor = Color.parseColor("#FF0000")
    }

    var streamerWidth: Float
        set(value) {
            streamerView.streamerWidth = value
        }
        get() = streamerView.streamerWidth
    var streamerHeightOffset: Float
        set(value) {
            streamerView.streamerHeightOffset = value
        }
        get() = streamerView.streamerHeightOffset

    var angleSize: Int
        set(value) {
            streamerView.angleSize = value
        }
        get() = streamerView.angleSize

    var streamerColor: Int
        set(value) {
            streamerView.streamerColor = value
        }
        get() = streamerView.streamerColor

    var animDuration: Long
        set(value) {
            streamerView.animDuration = value
        }
        get() = streamerView.animDuration


    override fun getBottomPlateView(): View? = null
    override fun getLoadingView(): StreamerConstraintLayout? = streamerView
    override fun getLoadingFailView(): View? = null

    fun setText(str: String) {
        tv.text = str
    }

    /**
     * 属性动画 默认是弱引用的，不必考虑释放问题
     */
    override fun onShowLoading(loadingView: View?) {
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

    override fun onStop(loadingView: View?, failView: View?) {
        streamerView.stop()
    }

}

