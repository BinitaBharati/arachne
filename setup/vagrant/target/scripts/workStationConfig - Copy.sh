#!/bin/bash

mkdir -p /home/vagrant/deploy
#Copy all the shared files under /vagrant/* /home/vagrant/deploy
#You can not run ansible-playbook from the /vagrant shared folder directly,
#as you can not run ansible-playbbok from /vagrant folder.
#See: https://stackoverflow.com/questions/18385925/error-when-running-ansible-playbook#
cp -R /vagrant/* /home/vagrant/deploy/


sudo apt-get update

#install dos2unix
sudo apt-get -y install dos2unix

#install ansible
sudo apt-add-repository ppa:ansible/ansible -y
sudo apt-get install ansible -y


#install sshpass
sudo apt-get install sshpass


sudo chown -R vagrant:vagrant /home/vagrant/deploy







