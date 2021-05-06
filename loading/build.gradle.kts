import com.foundation.widget.buildsrc.*
plugins {
    id("com.android.library")
    id("kotlin-android")
}
apply("publish.gradle.kts")

android {
    compileSdkVersion(AndroidConfig.compileSdkVersion)
    defaultConfig {
        minSdkVersion(AndroidConfig.minSdkVersion)
        targetSdkVersion(AndroidConfig.targetSdkVersion)
        versionCode = Publish.Version.versionCode
        versionName = Publish.Version.versionName
    }
    buildTypes {
        forEach {
            if(it.name == "release"){
                it.isMinifyEnabled = true
                it.consumerProguardFiles("proguard-rules.pro")
            }
        }
    }

    compileOptions {
        sourceCompatibility = AndroidConfig.Language.sourceCompatibility
        targetCompatibility = AndroidConfig.Language.targetCompatibility
    }
    kotlinOptions {
        jvmTarget = AndroidConfig.Language.jvmTarget
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
    implementation(Dependencies.Kotlin.kotlin_stdlib)
    implementation(Dependencies.AndroidX.core_ktx)
    implementation(Dependencies.AndroidX.appcompat)
    implementation(Dependencies.Material.material)
    implementation(Dependencies.AndroidX.constraintlayout)
}