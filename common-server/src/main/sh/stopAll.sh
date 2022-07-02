#!/bin/bash

echo "Find Procedure : discovery-server-0.0.1-SNAPSHOT.jar"
pid=$(ps -ef|grep java|grep discovery-server-0.0.1-SNAPSHOT.jar|awk '{print $2}')
if [ -n "$pid" ]
then
echo "Stop Procedure : discovery-server-0.0.1-SNAPSHOT.jar"
kill -9 $pid
fi

echo "Find Procedure : delegation-server-0.0.1-SNAPSHOT.jar"
pid=$(ps -ef|grep java|grep delegation-server-0.0.1-SNAPSHOT.jar|awk '{print $2}')
if [ -n "$pid" ]
then
echo "Stop Procedure : delegation-server-0.0.1-SNAPSHOT.jar"
kill -9 $pid
fi

echo "Find Procedure : contract-server-0.0.1-SNAPSHOT.jar"
pid=$(ps -ef|grep java|grep delegation-server-0.0.1-SNAPSHOT.jar|awk '{print $2}')
if [ -n "$pid" ]
then
echo "Stop Procedure : contract-server-0.0.1-SNAPSHOT.jar"
kill -9 $pid
fi

echo "Find Procedure : gateway-server-0.0.1-SNAPSHOT.jar"
pid=$(ps -ef|grep java|grep delegation-server-0.0.1-SNAPSHOT.jar|awk '{print $2}')
if [ -n "$pid" ]
then
echo "Stop Procedure : gateway-server-0.0.1-SNAPSHOT.jar"
kill -9 $pid
fi

echo "Find Procedure : test-server-0.0.1-SNAPSHOT.jar"
pid=$(ps -ef|grep java|grep delegation-server-0.0.1-SNAPSHOT.jar|awk '{print $2}')
if [ -n "$pid" ]
then
echo "Stop Procedure : test-server-0.0.1-SNAPSHOT.jar"
kill -9 $pid
fi

echo "Find Procedure : spring-boot-security-jwt-server-0.0.1-SNAPSHOT.jar"
pid=$(ps -ef|grep java|grep spring-boot-security-jwt-server-0.0.1-SNAPSHOT.jar|awk '{print $2}')
if [ -n "$pid" ]
then
echo "Stop Procedure : spring-boot-security-jwt-server-0.0.1-SNAPSHOT.jar"
kill -9 $pid
fi