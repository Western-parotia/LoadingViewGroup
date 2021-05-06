package com.foundation.widget.simple

import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.foundation.widget.loading.LoadingConfig
import com.foundation.widget.loading.LoadingViewGroup

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val lv = LoadingViewGroup(this)
//        setContentView(lv)
//        lv.setUndergroundImg(R.drawable.ic_launcher_background)
//        lv.setLoadingTxt("加载中。。。。。。。。")
    }
}