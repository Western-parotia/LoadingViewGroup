apply("com.android.application")
android {
    compileSdkVersion =28
    defaultConfig {
        applicationId ="net.coding.android_demo"
        minSdkVersion =15
        targetSdkVersion =28
        versionCode =1
        versionName ="1.0"
        testInstrumentationRunner ="android.support.test.runner.AndroidJUnitRunner"
    }

//    buildTypes {
//        getByName()
//        release {
//            minifyEnabled false
//            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
//        }
//    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation ("com.android.support:appcompat-v7:28.0.0-rc02")
    implementation ("com.android.support:support-v4:28.0.0-rc02")
    implementation ("com.android.support:recyclerview-v7:28.0.0-rc02")
    implementation ("com.android.support:design:28.0.0-rc02")
    testImplementation ("junit:junit:4.12")
    androidTestImplementation ("com.android.support.test:runner:1.0.2")
    androidTestImplementation ("com.android.support.test.espresso:espresso-core:3.0.2")
}