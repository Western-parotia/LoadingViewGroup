package com.foundation.widget.buildsrc

import java.io.File


/**
 *@Desc:
 *-
 *-
 *create by zhusw on 5/6/21 16:43
 */

private const val VERSION = "1.0"
private const val SNAPSHOT = true

object Publish {
    object Version {
        var versionName = VERSION
            private set
            get() = when (SNAPSHOT) {
                true -> "$field-SNAPSHOT"
                false -> field
            }
        const val versionCode = 1

        private fun getTimestamp(): String {
            return java.text.SimpleDateFormat(
                "yyyy-MM-dd-hh-mm-ss",
                java.util.Locale.CHINA
            ).format(java.util.Date(System.currentTimeMillis()))
        }

        fun getVersionTimestamp(): String {
            return "$versionName-${getTimestamp()}"
        }
    }

    object Maven {
        val groupId = "com.foundation.widget"
        val artifactId = "loading"

        private val pFile = File("local.properties")
        val codingArtifactsRepoUrl: String
            get() = getProperties(pFile, "codingArtifactsRepoUrl")
        val codingArtifactsGradleUsername: String
            get() = getProperties(pFile, "codingArtifactsGradleUsername")
        val codingArtifactsGradlePassword: String
            get() = getProperties(pFile, "codingArtifactsGradlePassword")

        init {
            "url=$codingArtifactsRepoUrl userName=$codingArtifactsGradleUsername pwd=$codingArtifactsGradlePassword".log(
                "Publish config=========:"
            )
        }


    }
}
