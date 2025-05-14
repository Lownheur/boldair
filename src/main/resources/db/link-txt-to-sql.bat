@ECHO OFF
CHCP 1252 >NUL
PROMPT $G
CD /D "%~dp0"
ECHO. & ECHO.

IF EXIST  1b-tables.txt DEL 1b-tables.txt
"C:\DEV25\utils\bin\elevate.exe" -c MKLINK  1b-tables.txt 1b-tables.sql ^& ECHO. ^& PAUSE

rem ECHO. & PAUSE