#!/bin/bash
# Launcher script for Airline Reservation System (JavaFX)

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$SCRIPT_DIR"

echo "Building Airline Reservation System..."
mkdir -p bin
find AirlineSystem/src -name "*.java" | xargs javac --module-path lib_mac --add-modules javafx.controls,javafx.fxml -d bin

if [ $? -eq 0 ]; then
    echo "Compilation successful. Launching Airline Reservation System GUI..."
    java --module-path lib_mac --add-modules javafx.controls,javafx.fxml -cp bin Main
else
    echo "Compilation failed."
fi
