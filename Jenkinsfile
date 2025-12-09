pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK11'
    }

    stages {
        stage('Подготовка') {
            steps {
                echo 'Клонирование репозитория...'
                checkout scm
            }
        }

        stage('Компиляция сервисов') {
            steps {
                echo 'Компиляция User Service...'
                dir('service1') {
                    sh 'mvn clean compile'
                }

                echo 'Компиляция Product Service...'
                dir('service2') {
                    sh 'mvn clean compile'
                }
            }
        }

        stage('Запуск сервисов') {
            steps {
                script {
                    echo 'Запуск User Service (Spring Boot)...'
                    sh '''
                        cd service1
                        nohup mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8080 > user-service.log 2>&1 &
                        echo $! > user-service.pid
                    '''

                    echo 'Запуск Product Service (Spring Boot)...'
                    sh '''
                        cd service2
                        nohup mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081 > product-service.log 2>&1 &
                        echo $! > product-service.pid
                    '''

                    // Ждем запуска Spring Boot приложений (им нужно больше времени)
                    sleep(time: 30, unit: 'SECONDS')
                }
            }
        }

        stage('Проверка здоровья') {
            steps {
                script {
                    echo 'Проверка User Service...'
                    sh 'curl -f http://localhost:8080/health || echo "User Service не отвечает"'

                    echo 'Проверка Product Service...'
                    sh 'curl -f http://localhost:8081/health || echo "Product Service не отвечает"'

                    // Ждем еще немного для полной инициализации
                    sleep(time: 10, unit: 'SECONDS')
                }
            }
        }

        stage('Интеграционные тесты') {
            steps {
                echo 'Выполнение интеграционных тестов...'
                dir('integration-tests') {
                    sh 'mvn clean test'
                }
            }
        }

        stage('Публикация результатов') {
            steps {
                junit 'integration-tests/target/surefire-reports/*.xml'
            }
        }
    }

    post {
        always {
            echo 'Остановка Spring Boot сервисов...'
            sh '''
                [ -f service1/user-service.pid ] && kill $(cat service1/user-service.pid) || true
                [ -f service2/product-service.pid ] && kill $(cat service2/product-service.pid) || true
                rm -f service1/user-service.pid service2/product-service.pid

                # Убиваем все процессы Java на нужных портах
                lsof -ti:8080 | xargs kill -9 2>/dev/null || true
                lsof -ti:8081 | xargs kill -9 2>/dev/null || true
            '''
        }
        success {
            echo '✅ Интеграционные тесты успешно пройдены!'
        }
        failure {
            echo '❌ Интеграционные тесты не пройдены'
        }
    }
}