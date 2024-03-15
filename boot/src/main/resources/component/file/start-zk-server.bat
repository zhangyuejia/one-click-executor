@echo off
echo 'start xxl-job'
for /F "tokens=5" %%a in ('netstat -ano ^| findstr :2181') do taskkill /F /PID %%a
cd d:
cd D:\Program Files\apache-zookeeper-3.9.1-bin\bin
zkServer.cmd