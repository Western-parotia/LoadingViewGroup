package com.buildsrc.kts

import org.gradle.api.JavaVersion

/**
 *@Desc:
 *-
 *-
 *create by zhusw on 5/6/21 16:09
 */
object AndroidConfig {
    const val compileSdkVersion = 30
    const val minSdkVersion = 21
    const val targetSdkVersion = 30

    object Language{
        const val jvmTarget = "1.8"
        val sourceCompatibility = JavaVersion.VERSION_1_8
        val targetCompatibility = JavaVersion.VERSION_1_8
    }

}