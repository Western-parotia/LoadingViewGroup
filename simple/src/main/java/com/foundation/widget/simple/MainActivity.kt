package com.foundation.widget.simple

import android.content.Context
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.foundation.widget.loading.NormalLoadingAdapter
import com.foundation.widget.loading.PageLoadingAdapter
import com.foundation.widget.simple.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.contentLoading.setLoadingAdapter(MyContentLoadingAdapter(this))
        binding.contentLoading.failViewClickListener = { _: View, _: Int, _: Any? ->
            Toast.makeText(this, "lc fail click", Toast.LENGTH_LONG).show()
        }
        binding.diyLoading.setLoadingAdapter(DiyLoading(this))

        binding.btnStart.setOnClickListener {
            binding.normalLoading.showLoading()
            binding.diyLoading.showLoading(true)
            binding.contentLoading.showLoading(true)
        }
        binding.btnStop.setOnClickListener {
            binding.normalLoading.stop()
            binding.diyLoading.stop()
            binding.contentLoading.stop()
        }
        binding.btnShowFail.setOnClickListener {
            binding.normalLoading.showLoadingFail()
            binding.diyLoading.showLoadingFail(false)
            binding.contentLoading.showLoadingFail(false)
        }

        binding.normalLoading.failViewClickListener = { _: View, _: Int, _: Any? ->
            Toast.makeText(this, "lv fail click", Toast.LENGTH_LONG).show()
        }
        binding.btnShow.setOnClickListener {
            binding.normalLoading.checkLoadingState()
            binding.contentLoading.checkLoadingState()
        }
    }

}

/**
 * 设置骨架图
 */
class MyContentLoadingAdapter(private val context: Context) : NormalLoadingAdapter(context) {

    override fun getBottomPlateView(): View {
        return AppCompatImageView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            background = ContextCompat.getDrawable(context, R.drawable.img_skeleton_screen)
        }
    }
}

/**
 * 自定义动画
 * 使用svg 作为loading 动画
 */
class DiyLoading(private val context: Context) : PageLoadingAdapter {
    private val loadingView = AppCompatImageView(context).apply {
        background = ContextCompat.getDrawable(context, R.drawable.dw_loading)
    }

    override fun getBottomPlateView(): View {
        return AppCompatImageView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            background = ContextCompat.getDrawable(context, R.drawable.sp_loading_bg2)
        }
    }

    override fun getLoadingFailView(): View? {
        return null
    }

    override fun getLoadingView(): View = loadingView

    override fun onShowLoading(loadingView: View) {
        if (loadingView.background is Animatable) {
            "onShowLoading Animatable".log("PageLoadingView")
            val anim = loadingView.background as Animatable
            anim.start()
        }
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
        if (loadingView?.background is Animatable) {
            val anim = loadingView.background as Animatable
            anim.stop()
        }
    }
}

private fun String.log(secTag: String) {
    println("MainActivity $secTag $this")
}