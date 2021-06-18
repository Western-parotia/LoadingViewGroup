package com.foundation.widget.simple

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.foundation.widget.loading.PageLoadingAdapter
import com.foundation.widget.loading.StreamerConstraintLayout
import com.foundation.widget.loading.StreamerPageLoadingAdapter
import com.foundation.widget.simple.databinding.ActivityMainBinding

internal val Float.dp get() = this * Resources.getSystem().displayMetrics.density + 0.5F
internal val Int.dp get() = this.toFloat().dp.toInt()

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.contentLoading.setLoadingAdapter(MyContentLoadingAdapter(this))

        val streamerView = StreamerConstraintLayout(this)
        val tv = TextView(this)
        tv.text = "正在加载..."
        tv.textSize = 10F.dp
        tv.typeface = Typeface.DEFAULT_BOLD
        tv.setTextColor(Color.parseColor("#dbdbdb"))
        val lp = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        streamerView.addView(tv, lp)
        streamerView.streamerColor = Color.parseColor("#FF0000")
        streamerView.animDuration = 1400L
        streamerView.angleSize = 80
        streamerView.streamerWidth = 10F.dp
        binding.streamerLoading.setLoadingAdapter(StreamerPageLoadingAdapter(streamerView))

        binding.btnStart.setOnClickListener {
            binding.normalLoading.showLoading(false)
            binding.contentLoading.showLoading(true)
            binding.streamerLoading.showLoading(false)
        }
        binding.btnStop.setOnClickListener {
            binding.normalLoading.stop()
            binding.contentLoading.stop()
            binding.streamerLoading.stop()
        }
        binding.btnShowFail.setOnClickListener {
            binding.normalLoading.showLoadingFail()
            binding.contentLoading.showLoadingFail(false, 0, "额外参数")
            binding.streamerLoading.showLoadingFail()
        }

        binding.btnShow.setOnClickListener {
            binding.normalLoading.checkLoadingState()
            binding.contentLoading.checkLoadingState()
            binding.streamerLoading.checkLoadingState()

        }
        binding.normalLoading.failViewEventListener = { _: View, _: Int, _: Any? ->
            Toast.makeText(this, "normalLoading fail click", Toast.LENGTH_LONG).show()
            binding.normalLoading.showLoading(false)
        }
        binding.streamerLoading.failViewEventListener = { _: View, _: Int, _: Any? ->
            Toast.makeText(this, "streamerLoading fail click", Toast.LENGTH_LONG).show()
            binding.streamerLoading.showLoading(false)
        }
        binding.contentLoading.failViewEventListener = { _: View, type: Int, extra: Any? ->
            Toast.makeText(
                this,
                "contentLoading fail clikc type:$type extra:$extra",
                Toast.LENGTH_LONG
            ).show()
            binding.contentLoading.showLoading(true)
        }
    }

}

/**
 * 自定义loading逻辑
 */
class MyContentLoadingAdapter(private val context: Context) :
    PageLoadingAdapter {

    /**
     * 设置骨架图
     */
    override fun getBottomPlateView(): View = AppCompatImageView(context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            350.dp
        )
        background = ContextCompat.getDrawable(context, R.drawable.img_skeleton_screen)
    }

    /**
     * 自定义加载动画的View
     */
    private val loadingView = AppCompatImageView(context).apply {
        background = ContextCompat.getDrawable(context, R.drawable.dw_loading)
    }

    override fun getLoadingView(): AppCompatImageView? = loadingView

    /**
     * 展示动画
     */
    override fun onShowLoading(loadingView: View?) {
        val anim = loadingView?.background as? Animatable
        anim?.start()
    }

    /**
     * 设置失败展示
     */
    override fun getLoadingFailView(): View? = LayoutInflater
        .from(context)
        .inflate(R.layout.fail, null).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

    /**
     * 设置失败响应事件
     */
    override fun onShowFail(
        failView: View,
        type: Int,
        extra: Any?,
        failViewEvent: (view: View, type: Int, extra: Any?) -> Unit
    ) {
        failView.findViewById<View>(R.id.btn).setOnClickListener {
            failViewEvent.invoke(failView, type, extra)
        }
    }

    /**
     * 停止动画
     */
    override fun onStop(loadingView: View?, failView: View?) {
        val anim = loadingView?.background as? Animatable
        anim?.stop()
    }

}

private fun String.log(secTag: String) {
    println("MainActivity $secTag $this")
}