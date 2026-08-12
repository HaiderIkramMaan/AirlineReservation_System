@echo off
setlocal EnableDelayedExpansion
cd /d "%~dp0"

if not exist "javafx-sdk\javafx-sdk-21.0.2\lib" (
    echo JavaFX SDK not found at: %CD%\javafx-sdk\javafx-sdk-21.0.2\lib
    echo Please ensure the JavaFX SDK is present in the project folder.
    pause
    exit /b 1
)

if not exist "out" mkdir out

set "JFX=%CD%\javafx-sdk\javafx-sdk-21.0.2\lib"

where javac >nul 2>nul
if errorlevel 1 (
    echo JDK not found in PATH.
    echo Install a JDK and try again.
    pause
    exit /b 1
)

set "SRC_FILES="
for /r "src" %%f in (*.java) do (
    set "SRC_FILES=!SRC_FILES! "%%~ff""
)

if not defined SRC_FILES (
    echo No Java source files were found in src.
    pause
    exit /b 1
)

rem Compile all Java sources into the out directory
javac --module-path "%JFX%" --add-modules javafx.controls,javafx.fxml -d out !SRC_FILES!
if errorlevel 1 (
    echo Compile failed.
    pause
    exit /b 1
)

rem Run the application
java --module-path "%JFX%" --add-modules javafx.controls,javafx.fxml -cp out Main
exit /b %errorlevel%
