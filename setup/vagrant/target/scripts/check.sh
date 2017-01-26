#!/usr/bin/env bash



#kill existing process


PID=`ps -eaf | grep java  | grep -v grep | awk '{print $2}'`


if [[ "" !=  "$PID" ]]; 
	then
  echo "killing $PID"
  
	sudo kill -9 $PID


fi


sudo rm -rf /home/vagrant/arachne/logs/* /home/vagrant/test.db /home/vagrant/output.log

