@echo off
setlocal EnableDelayedExpansion

start "Terminal" cmd /k "title DOCKER-COMPOSE && docker-compose up"

endlocal
