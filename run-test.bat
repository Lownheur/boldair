@echo off
echo Attempting to run ServiceInscriptionTest...

REM Check if gradle wrapper exists
if exist gradlew.bat (
    echo Using gradle wrapper...
    gradlew.bat test --tests "boldair.service.ServiceInscriptionTest"
) else if exist gradle\wrapper\gradle-wrapper.jar (
    echo Creating gradle wrapper...
    java -jar gradle\wrapper\gradle-wrapper.jar wrapper
    gradlew.bat test --tests "boldair.service.ServiceInscriptionTest"
) else (
    echo No gradle wrapper found, trying gradle directly...
    gradle test --tests "boldair.service.ServiceInscriptionTest"
)

pause
