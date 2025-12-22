@echo off
echo Limpando pasta bin...
rmdir /s /q bin 2>nul
mkdir bin

echo Compilando...
javac --module-path lib\javafx-sdk-25.0.1\lib ^
      --add-modules javafx.controls,javafx.fxml,javafx.base ^
      -cp ".;lib\sqlite-jdbc-3.51.1.0.jar" ^
      -d bin ^
      src\*.java

if %errorlevel% neq 0 (
    echo.
    echo ERRO NA COMPILACAO!
    pause
    exit /b
)

echo.
echo Executando o Sistema de Controle de Portaria...
java --module-path lib\javafx-sdk-25.0.1\lib ^
     --add-modules javafx.controls,javafx.fxml,javafx.base ^
     -cp ".;lib\sqlite-jdbc-3.51.1.0.jar;bin" ^
     ControlePortariaApp

pause