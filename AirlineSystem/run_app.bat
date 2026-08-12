@echo off
setlocal EnableDelayedExpansion
cd /d "%~dp0"

set "PROJECT_DIR=%~dp0"
set "JFX="

if exist "%PROJECT_DIR%javafx-sdk\lib" (
    set "JFX=%PROJECT_DIR%javafx-sdk\lib"
) else if exist "%PROJECT_DIR%javafx-sdk\javafx-sdk-21.0.2\lib" (
    set "JFX=%PROJECT_DIR%javafx-sdk\javafx-sdk-21.0.2\lib"
) else (
    for /d %%D in ("%PROJECT_DIR%javafx-sdk\*") do (
        if exist "%%~fD\lib" (
            set "JFX=%%~fD\lib"
        )
    )
)

if not defined JFX (
    echo JavaFX SDK not found.
    echo Expected a JavaFX SDK under: %PROJECT_DIR%javafx-sdk
    echo or: %PROJECT_DIR%javafx-sdk\javafx-sdk-21.0.2\lib
    echo Install or extract the JavaFX SDK and try again.
    pause
    exit /b 1
)

if not exist "out" mkdir out

if defined JAVA_HOME (
    set "JAVAC=%JAVA_HOME%\bin\javac.exe"
    set "JAVA_CMD=%JAVA_HOME%\bin\java.exe"
) else (
    where javac >nul 2>nul
    if errorlevel 1 (
        echo JDK not found in PATH.
        echo Install a JDK or set JAVA_HOME and try again.
        pause
        exit /b 1
    )
    set "JAVAC=javac"
    set "JAVA_CMD=java"
)

if not defined JAVA_HOME (
    where java >nul 2>nul
    if errorlevel 1 (
        echo Java runtime not found in PATH.
        echo Install a JDK or set JAVA_HOME and try again.
        pause
        exit /b 1
    )
)

set "SRC_FILES="
for /r "%PROJECT_DIR%src" %%f in (*.java) do (
    set "SRC_FILES=!SRC_FILES! "%%~ff""
)

if not defined SRC_FILES (
    echo No Java source files were found in src.
    pause
    exit /b 1
)

rem Compile all Java sources into the out directory
"%JAVAC%" --module-path "%JFX%" --add-modules javafx.controls,javafx.fxml -d out !SRC_FILES!
if errorlevel 1 (
    echo Compile failed.
    pause
    exit /b 1
)

rem Run the application
"%JAVA_CMD%" --module-path "%JFX%" --add-modules javafx.controls,javafx.fxml -cp out Main
exit /b %errorlevel%
