#!/bin/bash
#WORKSPACE
LOGDIR="/root/deployment"

NOW="discovery-server"
nohup java -jar ./SE2022-SoftwareTestsSystem/${NOW}/target/${NOW}-0.0.1-SNAPSHOT.jar > ${LOGDIR}/${NOW}.txt &
sleep 8

NOW="delegation-server"
nohup java -jar ./SE2022-SoftwareTestsSystem/${NOW}/target/${NOW}-0.0.1-SNAPSHOT-exec.jar > ${LOGDIR}/${NOW}.txt &

NOW="contract-server"
nohup java -jar ./deployment/${NOW}-0.0.1-SNAPSHOT-exec.jar > ${LOGDIR}/${NOW}.txt &

NOW="sample-server"
nohup java -jar ./SE2022-SoftwareTestsSystem/${NOW}/target/${NOW}-0.0.1-SNAPSHOT.jar > ${LOGDIR}/${NOW}.txt &

NOW="test-server"
nohup java -jar ./SE2022-SoftwareTestsSystem/${NOW}/target/${NOW}-0.0.1-SNAPSHOT.jar > ${LOGDIR}/${NOW}.txt &

NOW="spring-boot-security-jwt"
nohup java -jar ./SE2022-SoftwareTestsSystem/${NOW}/target/${NOW}-0.0.1-SNAPSHOT.jar > ${LOGDIR}/${NOW}.txt &

sleep 15

NOW="gateway-server"
nohup java -jar ./SE2022-SoftwareTestsSystem/${NOW}/target/${NOW}-0.0.1-SNAPSHOT.jar > ${LOGDIR}/${NOW}.txt &