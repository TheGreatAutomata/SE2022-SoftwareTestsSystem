#!/bin/bash

NOW="discovery-server"
pid=$(ps -ef|grep java|grep ${NOW}-0.0.1-SNAPSHOT.jar|awk '{print $2}')
if [ -n "$pid" ]
then
kill -9 $pid
fi

NOW="gateway-server"
pid=$(ps -ef|grep java|grep ${NOW}-0.0.1-SNAPSHOT.jar|awk '{print $2}')
if [ -n "$pid" ]
then
kill -9 $pid
fi

NOW="contract-server"
pid=$(ps -ef|grep java|grep ${NOW}-0.0.1-SNAPSHOT-exec.jar|awk '{print $2}')
if [ -n "$pid" ]
then
kill -9 $pid
fi

NOW="sample-server"
pid=$(ps -ef|grep java|grep ${NOW}-0.0.1-SNAPSHOT.jar|awk '{print $2}')
if [ -n "$pid" ]
then
kill -9 $pid


NOW="delegation-server"
pid=$(ps -ef|grep java|grep ${NOW}-0.0.1-SNAPSHOT-exec.jar|awk '{print $2}')
if [ -n "$pid" ]
then
kill -9 $pid
fi

NOW="test-server"
pid=$(ps -ef|grep java|grep ${NOW}-0.0.1-SNAPSHOT.jar|awk '{print $2}')
if [ -n "$pid" ]
then
kill -9 $pid
fi

NOW="spring-boot-security-jwt"
pid=$(ps -ef|grep java|grep ${NOW}-0.0.1-SNAPSHOT.jar|awk '{print $2}')
if [ -n "$pid" ]
then
kill -9 $pid
fi