@ECHO OFF
echo ^<?xml version="1.0" encoding="UTF-8"?^>
echo ^<Configuration status="warn"^>
echo 	^<Properties^>
echo 		^<Property name="basePath"^>%WORKSPACE%\src\test\java\office\sirion\logs
echo 		^</Property^>
echo 	^</Properties^>
echo.
echo 	^<Appenders^>
echo 		^<RollingFile name="fileLogger" fileName="${basePath}\\Application.log"
echo 			filePattern="${basePath}\\Application-%d{yyyy-MMM-dd}.log"^>
echo 			^<PatternLayout^>
echo 				^<pattern^>[%%-5level] %%d{yyyy-MMM-dd HH:mm:ss.SSS} [%%t] %%c{1} - %%msg%%n
echo 				^</pattern^>
echo 			^</PatternLayout^>
echo 			^<Policies^>
echo 				^<TimeBasedTriggeringPolicy interval="1"
echo 					modulate="true" /^>
echo 			^</Policies^>
echo 		^</RollingFile^>
echo 		^<Console name="console" target="SYSTEM_OUT"^>
echo 			^<PatternLayout
echo 				pattern="[%%-5level] %%d{yyyy-MMM-dd HH:mm:ss.SSS} [%%t] %%c{1} - %%msg%%n" /^>
echo 		^</Console^>
echo 	^</Appenders^>
echo 	^<Loggers^>
echo 		^<Logger name="com.howtodoinjava" level="debug" additivity="true"^>
echo 			^<appender-ref ref="fileLogger" level="debug" /^>
echo 		^</Logger^>
echo 		^<Root level="debug" additivity="false"^>
echo 			^<appender-ref ref="fileLogger" /^>
echo 			^<appender-ref ref="console" /^>
echo 		^</Root^>
echo.
echo 	^</Loggers^>
echo ^</Configuration^>