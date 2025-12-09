pipeline {
    agent any
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Check Tools') {
            steps {
                sh '''
                    echo "=== Checking installed tools ==="
                    java -version
                    mvn --version
                    echo "=== Tools check completed ==="
                '''
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }
        
        stage('Integration Tests') {
            steps {
                script {
                    echo "=== Running Integration Tests ==="
                    echo "Mock: Starting User Service on port 8080"
                    echo "Mock: Starting Product Service on port 8081"
                    sleep(time: 3, unit: 'SECONDS')
                    echo "Mock: Services are running"
                    
                    sh 'cd integration-tests && mvn test'
                }
            }
            post {
                always {
                    junit 'integration-tests/target/surefire-reports/*.xml'
                }
            }
        }
    }
    
    post {
        success {
            echo 'INTEGRATION TESTS PASSED!'
        }
        failure {
            echo 'INTEGRATION TESTS FAILED'
        }
    }
}
