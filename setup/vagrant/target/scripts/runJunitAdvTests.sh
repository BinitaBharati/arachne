#!/usr/bin/env bash



cd /home/vagrant/arachne/
rm -rf runSanity.log logs/junit*.log
java -Djava.net.preferIPv4Stack=true -cp arachne-0.0.1-SNAPSHOT-fat-tests.jar com.github.binitabharati.arachne.test.sanity.RunSanity AdvancedSanity>> runSanity.log 2>&1 &