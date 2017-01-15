#!/bin/bash








#install dos2unix




sudo apt-get -y install dos2unix




dos2unix /vagrant/target/scripts/install_java.sh /vagrant/target/scripts/install_java.sh










#install java



chmod +x /vagrant/target/scripts/install_java.sh




/vagrant/target/scripts/install_java.sh









#install traceroute




sudo apt-get install traceroute






#add multicast route



#sudo route add -net 224.0.0.0 netmask 240.0.0.0 eth1






sudo echo "#Multicast route" >> /etc/network/interfaces



sudo echo "up route add -net 224.0.0.0 netmask 240.0.0.0 eth1" >> /etc/network/interfaces







#sudo /etc/init.d/networking restart






sudo ifdown eth1 && sudo ifup eth1






#Stop different subnets from cross communication via NAT interface. Only allow communication across subnets via router.




#10.0.2.2 is the NAT gateway




#sudo route del -net 0.0.0.0 gw 10.0.2.2 netmask 0.0.0.0 dev eth0
