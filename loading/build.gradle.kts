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
    implementation(Dependencies.Glide.glide)
}

val sourceCode = tasks.register("sourceCode", org.gradle.jvm.tasks.Jar::class.java) {
    from(android.sourceSets.getByName("main").java.srcDirs)
    classifier = "sources"
}.get()

publishing {
    val versionName = Publish.Version.versionName
    val groupId = Publish.Maven.groupId
    val artifactId = Publish.Maven.artifactId

    publications {
        create<MavenPublication>("LoadingView") {
            setGroupId(groupId)
            setArtifactId(artifactId)
            version = versionName
            artifact(sourceCode)
            afterEvaluate {
                artifact(tasks.getByName("bundleReleaseAar"))
            }
//            artifact("$buildDir/outputs/aar/loading-release.aar")//直接制定文件
            " allDependencies=${configurations.implementation.get().allDependencies.size}".log("dep============")
            pom.withXml {
                val dependenciesNode = asNode().appendNode("dependencies")
                configurations.implementation.get().allDependencies.forEach {
                    if (it.version != "unspecified" && it.name != "unspecified") {
                        val depNode = dependenciesNode.appendNode("dependency")
                        depNode.appendNode("groupId", it.group)
                        depNode.appendNode("artifactId", it.name)
                        depNode.appendNode("version", it.version)
                    }
                }
            }

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

}


fun String.log(tag: String) {
    println("$tag:$this")
}