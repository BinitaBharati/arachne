#!/usr/bin/env bash


nohup java -Djava.net.preferIPv4Stack=true -Xms1048m -Xmx1048m -cp arachne-0.0.1-SNAPSHOT.jar com.github.binitabharati.arachne.service.Main >> output.log 2>&1 &
