pipeline {
  agent any
  environment {
    ARTIFACT_IMAGE="${ARTIFACT_BASE}/${PROJECT_NAME}/${DEPOT_NAME}/${DEPOT_NAME}"
  }
  stages {
    stage('检出') {
      agent any
      steps {
        checkout([$class: 'GitSCM', branches: [[name: env.GIT_BUILD_REF]],
                          userRemoteConfigs: [[url: env.GIT_REPO_URL, credentialsId: env.CREDENTIALS_ID]]])
      }
    }
    stage('构建 APK') {
      agent any
      steps {
        sh './gradlew assemble'
      }
    }
    stage('归档 APK') {
      agent any
      steps {
        codingArtifactsGeneric(files:"app/build/outputs/apk/debug/*.apk", repoName: env.DEPOT_NAME, version: env.GIT_BUILD_REF)
      }
    }
  }
}