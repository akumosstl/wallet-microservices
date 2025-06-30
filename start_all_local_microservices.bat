@echo off
setlocal EnableDelayedExpansion

start "Terminal" cmd /k "title EUREKA && cd /d eureka-service && mvn spring-boot:run"

start "Terminal" cmd /k "title INFRA-KAFKA && docker-compose -f docker-compose-infra.yml up"

timeout /t 60 /nobreak

start "Terminal" cmd /k "title GATEWAY && cd /d gateway-service && mvn spring-boot:run"

start "Terminal" cmd /k "title AUTH && cd /d auth-service && mvn spring-boot:run"

timeout /t 60 /nobreak

start "Terminal" cmd /k "title ACCOUNT && cd /d account-service && mvn spring-boot:run"

start "Terminal" cmd /k "title TRANSACTION && cd /d transaction-service && mvn spring-boot:run"

start "Terminal" cmd /k "title EVENT && cd /d event-service && mvn spring-boot:run"

endlocal
