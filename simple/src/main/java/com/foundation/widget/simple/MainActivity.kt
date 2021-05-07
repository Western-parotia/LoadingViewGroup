package com.foundation.widget.simple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.foundation.widget.loading.LoadingViewGroup

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        val lv = LoadingViewGroup(this)
        setContentView(lv)
//        lv.alpha = 0F
//        lv.animate().alpha(1F).rotation(350F).setDuration(4000).start()

    }
}