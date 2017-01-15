#!/bin/bash

/vagrant/target/scripts/common.sh

#add multicast route
sudo echo "up route add -net 224.0.0.0 netmask 240.0.0.0 eth2" >> /etc/network/interfaces
sudo ifdown eth2 && sudo ifup eth2


#Configure this m/c to work as a router.Enable ipv4 forwarding.

#http://www.ducea.com/2006/08/01/how-to-enable-ip-forwarding-in-linux/
cp /etc/sysctl.conf temp
sudo echo 'net.ipv4.ip_forward = 1' >> temp
sudo cp temp /etc/sysctl.conf
sudo sysctl -p /etc/sysctl.conf