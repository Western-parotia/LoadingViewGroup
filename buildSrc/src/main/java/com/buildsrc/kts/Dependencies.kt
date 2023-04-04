package com.buildsrc.kts

object Dependencies {
    const val kotlinVersion = "1.6.21"

    object Kotlin {
        const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"
    }

    object Foundation {
        const val utils = "com.foundation.widget:utils:1.0.0"
        const val activityFragment = "com.foundation.app:activity-fragment:1.0.8"
        const val loadingView = "com.foundation.widget:loadingview:1.1.9"
        const val recyclerviewAdapter =
            "com.foundation.widget:convenient-recyclerview-adapter:1.0.6"
    }

    object AndroidX {
        const val core_ktx = "androidx.core:core-ktx:1.6.0"
        const val appcompat = "androidx.appcompat:appcompat:1.3.0"
        const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.0.4"

    }

    object Material {
        const val material = "com.google.android.material:material:1.3.0"
    }

    object OpenSourceLibrary {
        private const val smartVersion = "2.0.5"
        const val smartRefreshLayout = "io.github.scwang90:refresh-layout-kernel:$smartVersion"
        const val smartRefreshLayoutHeader =
            "io.github.scwang90:refresh-header-material:$smartVersion"
        const val smartRefreshLayoutFooter =
            "io.github.scwang90:refresh-footer-classics:$smartVersion"

        const val flexBox = "com.google.android:flexbox:2.0.1"
    }

    object Google {
        const val gson = "com.google.code.gson:gson:2.8.6"
    }

    object Company {
//        val json = "com.foundation.service:Json:${Publish.Version.versionName}"
    }
}