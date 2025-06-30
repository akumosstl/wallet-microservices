rem start "Terminal" cmd /k "title CLEANUP-DOCKER && for /F %i in ('docker ps -a -q') do docker rm -f %i"

@echo off
echo Stopping all running Docker containers...
docker stop $(docker ps -q)

echo Removing all Docker containers...
docker rm $(docker ps -aq)

echo All containers have been stopped and removed.
pause
