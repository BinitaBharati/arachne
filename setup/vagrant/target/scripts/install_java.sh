#!/bin/bash




sudo apt-get install -y software-properties-common

sudo add-apt-repository -y ppa:webupd8team/java


sudo apt-get update

sudo echo debconf shared/accepted-oracle-license-v1-1 select true | sudo debconf-set-selections


sudo apt-get install -y oracle-java8-installer