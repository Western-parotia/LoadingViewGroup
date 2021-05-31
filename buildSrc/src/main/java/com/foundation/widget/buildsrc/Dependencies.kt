package com.foundation.widget.buildsrc

/**
 *@Desc:
 *-
 *-依赖声明
 *create by zhusw on 5/6/21 15:45
 */
object Dependencies {
    object Kotlin{
        const val version = "1.4.32"

        /**
         * kotlin 语言核心库，像 let这种操作域拓展
         */
        const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:$version"
    }

    object AndroidX {
        /**
         * kotlin 标准库，各种推展方法，像 foreach什么的
         */
        const val core_ktx = "androidx.core:core-ktx:1.3.2"
        const val appcompat = "androidx.appcompat:appcompat:1.2.0"
        const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.0.4"

    }

    object Material {
        const val material = "com.google.android.material:material:1.3.0"
    }

    object Glide {
        const val glide = "com.github.bumptech.glide:glide:4.10.0"
        const val compiler = "com.github.bumptech.glide:compiler:4.10.0"
    }
}
