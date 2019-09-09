@echo off
rem 去掉下面的rem可以指定运行的jdk
rem set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_181
rem set CLASSPATH=.;%JAVA_HOME%\lib\dt.jar;%JAVA_HOMe%\lib\tools.jar;
rem set Path=%JAVA_HOME%\bin;
java -jar make-copylist-1.2.0.jar
pause