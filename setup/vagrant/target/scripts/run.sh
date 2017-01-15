#!/usr/bin/env bash





cd /home/vagrant/arachne/


rm -rf logs/* test.db nohup.out


sudo nohup java -Djava.net.preferIPv4Stack=true -Xms850m -Xmx850m -cp arachne-0.0.1-SNAPSHOT.jar com.github.binitabharati.arachne.service.Main >> output.log 2>&1 &
