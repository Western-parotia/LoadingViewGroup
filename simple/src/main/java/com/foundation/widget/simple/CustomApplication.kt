package com.foundation.widget.simple

import android.app.Application
import android.graphics.Color
import com.foundation.widget.loading.GlobalLoadingConfig

/**
 * create by zhusw on 7/2/21 19:55
 */
class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        GlobalLoadingConfig.onInitForegroundColor = Color.BLUE

    }
}