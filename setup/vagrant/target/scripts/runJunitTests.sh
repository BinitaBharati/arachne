#!/usr/bin/env bash



cd /home/vagrant/arachne/
rm -rf runSanity.log logs/junit*.log
nohup java -Djava.net.preferIPv4Stack=true -cp arachne-test-0.0.1-SNAPSHOT.jar com.github.binitabharati.arachne.test.network2.sanity.RunSanity >> runSanity.log 2>&1 &