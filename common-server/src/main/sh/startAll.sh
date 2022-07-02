#!/bin/bash
#WORKSPACE
LOGDIR="/root/deployment"

NOW="discovery-server"
nohup java -jar "${WORKSPACE}/${NOW}/target/${NOW}-0.0.1-SNAPSHOT.jar" > "${LOGDIR}/${NOW}.txt" &
sleep 10

NOW="gateway-server"
nohup java -jar "${WORKSPACE}/${NOW}/target/${NOW}-0.0.1-SNAPSHOT.jar" > "${LOGDIR}/${NOW}.txt" &

