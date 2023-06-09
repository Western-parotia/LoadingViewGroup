import com.buildsrc.kts.Dependencies

plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdkVersion(30)

    defaultConfig {
        applicationId = "com.foundation.widget.simple"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        create("normalSign") {
            storeFile = file("test.jks")
            storePassword = "android"
            keyAlias = "android"
            keyPassword = "android"
        }
    }
    buildTypes {
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("normalSign")
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("normalSign")
        }
    }
    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

configurations.all {
    resolutionStrategy {
        //版本号会改变
        cacheDynamicVersionsFor(10, TimeUnit.SECONDS)
        //版本号不变,1.0-SNAPSHOT 这种应该实时更新
        cacheChangingModulesFor(10, TimeUnit.SECONDS)

    }
}
dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
    implementation(Dependencies.Kotlin.kotlin_stdlib)
    implementation(Dependencies.AndroidX.core_ktx)
    implementation(Dependencies.AndroidX.appcompat)
    implementation(Dependencies.Material.material)
    implementation(Dependencies.AndroidX.constraintlayout)
    implementation(Dependencies.OpenSourceLibrary.flexBox)
//    implementation("com.foundation.widget:loading:1.0.2-SNAPSHOT")
    implementation(project(":loading"))
//    implementation(Dependencies.Glide.glide)
//    implementation(Dependencies.Glide.compiler)

}
