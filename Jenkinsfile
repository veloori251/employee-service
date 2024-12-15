pipeline{
    agent any

    environment {
        DOCKER_IMAGE = "employee-service"
        DOCKER_TAG = "1.0"
        DOCKER_HUB = "vamsi5563/employee-service"
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    checkout scm
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    sh 'mvn clean test'
                }
            }
        }

        stage('Build Application') {
            steps {
                script {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build docker image') {
            steps {
                script {
                    sh 'docker build -t ${DOCKER_HUB}:${DOCKER_TAG} .'
                }
            }
        }

        stage('Login to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com','DOCKER_HUB_TOKEN')
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com','DOCKER_HUB_TOKEN') {
                        sh 'docker push ${DOCKER_HUB}:${DOCKER_TAG}'
                    }
                }
            }
        }

        stage('Deploy Application') {
            steps {
                script {
                    sh 'docker run -d -p 8081:8081 ${DOCKER_HUB}:${DOCKER_TAG}'
                }
            }
        }
    }

    post {
        success {
            echo 'Build and Deploy successful!'
        }
        failure {
            echo 'Build or Deploy failed!'
        }
    }
}