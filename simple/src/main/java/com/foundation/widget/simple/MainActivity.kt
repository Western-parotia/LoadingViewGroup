package com.foundation.widget.simple

import android.content.Context
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
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
        binding.lc.loadingView.loadingAdapter = MyContentLoadingAdapter(this)
        binding.lv2.loadingAdapter = DiyLoading(this)

        binding.btnStart.setOnClickListener {
            binding.lv.showLoading()
            binding.lv2.showLoading()
            binding.lc.loadingView.showLoading()
        }
        binding.btnStop.setOnClickListener {
            binding.lv.stop()
            binding.lv2.stop()
            binding.lc.loadingView.stop()
        }
        binding.btnShowFail.setOnClickListener {
            binding.lv.showLoadingFail(true)
            binding.lv2.showLoadingFail(false)
            binding.lc.loadingView.showLoadingFail(true)
        }
        binding.lc.loadingView.failViewClickListener = {
            Toast.makeText(MainActivity@ this, "lc fail click", Toast.LENGTH_LONG).show()
        }
        binding.lv.failViewClickListener = {
            Toast.makeText(MainActivity@ this, "lv fail click", Toast.LENGTH_LONG).show()
        }
        binding.btnShow.setOnClickListener {
            binding.lv.checkLoadingState()
            binding.lc.loadingView.checkLoadingState()
        }
    }

}

class MyContentLoadingAdapter(private val context: Context) : NormalLoadingAdapter(context) {
    override fun showBackgroundImg(): Boolean = true

    //    override fun getBackground(): Drawable? {
//        return ContextCompat.getDrawable(context, R.drawable.img_skeleton_screen)
//    }
    override fun getBackground(): Drawable? = null
}

/**
 * 使用svg 作为loading 动画
 */
class DiyLoading(private val context: Context) : PageLoadingAdapter {
    private val loadingView = AppCompatImageView(context).apply {
        background = ContextCompat.getDrawable(context, R.drawable.dw_loading)
    }

    override fun showBackgroundImg(): Boolean = true

    override fun getBackground(): Drawable? {
        return ContextCompat.getDrawable(context, R.drawable.sp_loading_bg2)
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

    override fun onShowFail(failView: View, type: Int, extra: Any?) {

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