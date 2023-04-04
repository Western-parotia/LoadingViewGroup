import com.buildsrc.kts.Publish


plugins {
    id("com.android.library")
    id("kotlin-android")
    `maven-publish`
}

apply("common.gradle")

val versionTimestamp = Publish.Version.getVersionTimestamp()

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
    afterEvaluate {
        buildTypes.forEach {
            it.buildConfigField("String", "versionName", "\"${Publish.Version.versionName}\"")
            it.buildConfigField("String", "versionTimeStamp", "\"$versionTimestamp\"")
        }
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
    implementation(com.buildsrc.kts.Dependencies.Kotlin.kotlin_stdlib)
    implementation(com.buildsrc.kts.Dependencies.AndroidX.core_ktx)
    implementation(com.buildsrc.kts.Dependencies.AndroidX.constraintlayout)
}

val sourceCodeTask: Jar = tasks.register("sourceCode", Jar::class.java) {
    from(android.sourceSets.getByName("main").java.srcDirs)
    classifier = "sources"
}
    .get()


tasks.register("createGitTagAndPush", Exec::class.java) {
    commandLine("git", "push", "origin", versionTimestamp)
}
    .get()
    .dependsOn(tasks.register("createGitTag", Exec::class.java) {
        commandLine("git", "tag", versionTimestamp, "-m", "autoCreateWithMavenPublish")
    })

publishing {
    publications {
        create<MavenPublication>("tools") {
            groupId = Publish.Maven.getThreePackage(projectDir)
            artifactId = Publish.Version.artifactId
            version = Publish.Version.versionName
            artifact(sourceCodeTask)
            afterEvaluate {//在脚本读取完成后绑定
                val bundleReleaseAarTask: Task = tasks.getByName("bundleReleaseAar")
                bundleReleaseAarTask.finalizedBy("createGitTagAndPush")
                artifact(bundleReleaseAarTask)
            }
//            artifact("$buildDir/outputs/aar/loading-release.aar")//直接制定文件
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
            if (Publish.SNAPSHOT) {
                Publish.Maven.aliyunSnapshotRepositories(this)
            } else {
                Publish.Maven.aliyunReleaseRepositories(this)
            }
        }
    }
}
fun String.log(tag: String) {
    println("$tag:$this")
}