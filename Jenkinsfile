pipeline {
    agent any

    tools {
        // Must match the tool names configured in Jenkins -> Global Tool Configuration
        maven 'Maven' 
        jdk 'JDK21'
    }

    stages {
        stage('Checkout Code') {
            steps {
                echo 'Checking out code from Git repository...'
                checkout scm
            }
        }

        stage('Compile') {
            steps {
                echo 'Compiling Java Snake Engine source files...'
                bat 'mvn clean compile'
            }
        }

        stage('Package App') {
            steps {
                echo 'Packaging executable JAR...'
                bat 'mvn package -DskipTests'
            }
        }

        stage('Archive Artifacts') {
            steps {
                // Save the built JAR file in Jenkins for download
                archiveArtifacts artifacts: 'target/*-app.jar', allowEmptyArchive: false
            }
        }
    }

    post {
        success {
            echo 'Build successful! Executable JAR is available in artifacts.'
        }
        failure {
            echo 'Build failed. Check compiler output above.'
        }
    }
}