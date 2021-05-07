package com.foundation.widget.simple

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.foundation.widget.loading.NormalLoadingAdapter
import com.foundation.widget.simple.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lc.loadingView.loadingAdapter = MyContentLoadingAdapter(this)
        binding.lv.loadingAdapter = MySingleLoadingAdapter(this)
        binding.btnStart.setOnClickListener {
            binding.lv.showLoading()
            binding.lc.loadingView.showLoading()
        }
        binding.btnStop.setOnClickListener {
            binding.lv.stopLoading()
            binding.lc.loadingView.stopLoading()
        }
        binding.btnShowFail.setOnClickListener {
            binding.lv.showLoadingFail(true)
            binding.lc.loadingView.showLoadingFail(true)
        }
        binding.lc.loadingView.failViewClickListener = {
            Toast.makeText(MainActivity@ this, "lc fail click", Toast.LENGTH_LONG).show()
        }
        binding.lv.failViewClickListener = {
            Toast.makeText(MainActivity@ this, "lv fail click", Toast.LENGTH_LONG).show()
        }
        binding.btnShow.setOnClickListener {
            binding.lv.showLoadingState()
            binding.lc.loadingView.showLoadingState()
        }
    }

}

class MySingleLoadingAdapter(private val context: Context) : NormalLoadingAdapter(context) {
    override fun showBackgroundImg(): Boolean = true
    override fun getLoadingBackground(): Drawable? =
        ContextCompat.getDrawable(context, R.drawable.sp_loading_bg1)
}

class MyContentLoadingAdapter(private val context: Context) : NormalLoadingAdapter(context) {
    override fun showBackgroundImg(): Boolean = true
    override fun getLoadingBackground(): Drawable? {
        return ContextCompat.getDrawable(context, R.drawable.img_skeleton_screen)

    }
}