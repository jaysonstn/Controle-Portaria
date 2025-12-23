@echo off
echo.
echo ================================================
echo   SISTEMA DE CONTROLE DE ACESSO - PORTARIA
echo ================================================
echo.

echo Limpando pasta bin...
rmdir /s /q bin 2>nul
mkdir bin

echo.
echo Compilando fontes...
javac --module-path lib\javafx-sdk-25.0.1\lib ^
      --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics ^
      -cp ".;lib\sqlite-jdbc-3.51.1.0.jar" ^
      -d bin ^
      src\br\com\portaria\*.java ^
      src\br\com\portaria\model\*.java ^
      src\br\com\portaria\dao\*.java

if %errorlevel% neq 0 (
    echo.
    echo *** ERRO DURANTE A COMPILACAO! ***
    echo Verifique os erros acima.
    pause
    exit /b
)

echo.
echo Compilacao concluida com sucesso!
echo.

echo Executando a aplicacao...
java --module-path lib\javafx-sdk-25.0.1\lib ^
     --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics ^
     --enable-native-access=javafx.graphics ^
     --enable-native-access=ALL-UNNAMED ^
     -cp ".;lib\sqlite-jdbc-3.51.1.0.jar;bin" ^
     br.com.portaria.ControlePortariaApp

echo.
echo Aplicacao encerrada.
pause