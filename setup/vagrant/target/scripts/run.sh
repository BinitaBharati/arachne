#!/usr/bin/env bash



cd /home/vagrant/arachne/
rm -rf test.db
nohup java -Djava.net.preferIPv4Stack=true -Xms1048m -Xmx1048m -cp arachne-core-0.0.1-SNAPSHOT.jar com.github.binitabharati.arachne.service.Main >> output.log 2>&1 &
