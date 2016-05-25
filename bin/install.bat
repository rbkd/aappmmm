@echo off
echo [INFO] Install apm.
cd %~dp0
cd ..
call mvn clean install -pl . -Dmaven.test.skip=true  

echo [INFO] Install apm/parent.
cd %~dp0
cd ../parent
call mvn clean install -pl . -Dmaven.test.skip=true


echo [INFO] Install common.
cd %~dp0
cd ../common
call mvn clean install -pl .  -Dmaven.test.skip=true

cd ../bin
pause