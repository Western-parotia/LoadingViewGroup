import com.foundation.widget.buildsrc.Dependencies
import com.foundation.widget.buildsrc.Publish

plugins {
    id("com.android.library")
    id("kotlin-android")
    `maven-publish`
}
apply("common.gradle")
android {
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            isMinifyEnabled = false
//            consumerProguardFiles("consumer-rules.pro") lib单独打包 此属性是无效的
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildTypes.forEach {
        it.buildConfigField("Integer", "versionCode", Publish.Version.versionCode.toString())
        it.buildConfigField("String", "versionName", "\"${Publish.Version.versionName}\"")
        it.buildConfigField("String", "versionTimeStamp", "\"${Publish.Version.versionTimeStamp}\"")
    }
    sourceSets {
        getByName("main") {
            java {
                srcDirs("src/main/java")
            }
        }
    }
}

dependencies {
    implementation(Dependencies.Kotlin.kotlin_stdlib)
    implementation(Dependencies.AndroidX.core_ktx)
    implementation(Dependencies.AndroidX.appcompat)
    implementation(Dependencies.AndroidX.constraintlayout)
}


publishing {
    val versionName = Publish.Version.versionName
    val groupId = Publish.Maven.groupId
    val artifactId = Publish.Maven.artifactId

    publications {
        create<MavenPublication>("LoadingView") {
            setGroupId(groupId)
            setArtifactId(artifactId)
            version = versionName
            artifact("$buildDir/outputs/aar/loading-release.aar")
        }
        repositories {
            maven {
                setUrl(Publish.Maven.codingArtifactsRepoUrl)
                credentials {
                    username = Publish.Maven.codingArtifactsGradleUsername
                    password = Publish.Maven.codingArtifactsGradlePassword
                }
            }
        }
    }
    tasks.getByName("assemble").doFirst {
        "maven Task: assembleRelease doFirst".log("task==============")
    }
    tasks.filter {
        it.name.contains("ToMaven")
    }.forEach { t ->
        t.dependsOn(tasks.getByName("assemble"))
    }
}


fun String.log(tag: String) {
    println("$tag:$this")
}