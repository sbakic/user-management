@ECHO OFF

SET COMPOSE_FILE_PATH=%CD%\docker-compose.yml

SET LOG_FILE_PATH=%CD%\application.log

SET GRADLE_EXEC=.\gradlew.bat

IF [%1]==[] (
    echo "Usage: %0 {build_start|start|stop|tail}"
    GOTO END
)

IF %1==build_start (
    CALL :down
    CALL :build
    CALL :start
    CALL :tail
    GOTO END
)
IF %1==start (
    CALL :start
    CALL :tail
    GOTO END
)
IF %1==stop (
    CALL :down
    GOTO END
)
IF %1==tail (
    CALL :tail
    GOTO END
)
echo "Usage: %0 {build_start|start|stop|tail}"
:END
EXIT /B %ERRORLEVEL%

:start
    docker-compose -f "%COMPOSE_FILE_PATH%" up --build -d
EXIT /B 0
:down
    if exist "%COMPOSE_FILE_PATH%" (
        docker-compose -f "%COMPOSE_FILE_PATH%" down
    )
EXIT /B 0
:build
    type NUL > "%LOG_FILE_PATH%"
    call %GRADLE_EXEC% build
EXIT /B 0
:tail
    docker-compose -f "%COMPOSE_FILE_PATH%" logs -f
EXIT /B 0
