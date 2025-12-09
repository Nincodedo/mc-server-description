pipeline {
    agent none

    stages {
        stage('Gradle Build') {
            steps {
                sh "./gradlew build"
            }
            agent any
            tools {
                jdk 'jdk25'
            }
            post {
                success {
                    archiveArtifacts 'build/libs/**.jar'
                }
            }
        }
    }
    post {
        regression {
            emailext attachLog: true, body: "${env.BUILD_URL}", compressLog: true, recipientProviders: [buildUser(), developers()], subject: "${env.JOB_NAME} - Build # ${env.BUILD_NUMBER} - ${currentBuild.currentResult}!"
        }
    }
}
