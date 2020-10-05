@echo off
cls
echo ------------------------------------------------------------------------------------
echo -                           DESKTOP DEBUG MOD
echo ------------------------------------------------------------------------------------
set INPUT=

java -Xms512m -Xmx1024m -jar "C:\ilegra\ilegrafileanalyze.jar" %INPUT%
set /p option=Fim. Presione uma tecla para finalizar.