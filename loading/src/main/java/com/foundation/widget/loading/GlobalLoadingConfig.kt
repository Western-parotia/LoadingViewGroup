package com.foundation.widget.loading

import android.graphics.Color
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 配置只能设置一次
 * 如果已经在xml 中设置了前景色，那么此配置不会生效
 * create by zhusw on 7/2/21 20:12
 */
object GlobalLoadingConfig {
    private val onInitForegroundColorMonitor = AtomicBoolean(false)
    var onInitForegroundColor = Color.WHITE
        set(value) {
            if (!onInitForegroundColorMonitor.get()) {
                onInitForegroundColorMonitor.set(true)
                field = value
            }
        }
}