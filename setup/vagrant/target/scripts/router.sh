#!/bin/bash




/vagrant/target/scripts/common.sh





#Configure this m/c to work as a router.Enable ipv4 forwarding.


#http://www.ducea.com/2006/08/01/how-to-enable-ip-forwarding-in-linux/


cp /etc/sysctl.conf temp


sudo echo 'net.ipv4.ip_forward = 1' >> temp


sudo cp temp /etc/sysctl.conf


sudo sysctl -p /etc/sysctl.conf