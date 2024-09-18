The load-test folder contains automated load tests for the underwriting app.

The test cases have to be tailored to the environment they are executed in.

cuws-load-test.bat starts the load test in openshift test
cuws-load-test-on-prem.bat starts the load test in the on prem test environment

The .bat files might have to be modified to match the JMETER_HOME of the machine it's executed

Test Plan: https://brmb.atlassian.net/wiki/x/AgAjog

Test Scenarios: https://brmb.atlassian.net/wiki/spaces/PISR/pages/2720235532/Underwriting+3.4.1+Load+Test+Scenarios

Test Execution: https://brmb.atlassian.net/browse/PIM-1561