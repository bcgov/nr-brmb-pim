@echo on
set JMETER_HOME=..\..\..\apache-jmeter-5.6.3
set JMETER_CUWS_THREADS=1
start cmd.exe /c %JMETER_HOME%\bin\jmeter -n -Jthreads=%JMETER_CUWS_THREADS% -t .\cuws-load-test.jmx -l CUWS_Test_Results.jtl