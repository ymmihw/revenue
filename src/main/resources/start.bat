title=urcc-ui
@echo off
cd 
set revenue=%~dp0
set JAVA_HOME=%revenue%\java

set para=--revenue.home=%revenue%

"%JAVA_HOME%\bin\java.exe" -Dlog.home=%revenue%\logs -Dlogging.config=file:\%revenue%\logback.xml -server -Dfile.encoding=UTF-8 -jar revenue-0.0.1-SNAPSHOT.war %para%