#!/usr/bin/env bash


#sleep for 5 secs. Wait for all sockets to die.
sleep 5




cd /home/vagrant/arachne/


rm -rf logs/* test.db nohup.out output.log

sudo nohup java -Djava.net.preferIPv4Stack=true -Xms1048m -Xmx1048m -cp arachne-0.0.1-SNAPSHOT.jar com.github.binitabharati.arachne.service.Main >> output.log 2>&1 &
