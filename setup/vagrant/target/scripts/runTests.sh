#!/bin/bash

cd /home/vagrant/deploy
#Recursively run dos2unix on all files under deploy folder
find . -type f -exec dos2unix {} \;
chmod 666 playbook-run-tests.yml ansible_inventory
ansible-playbook playbook-run-tests.yml -i ansible_inventory

#Now, print all the test results one by one.
#sleep for some time to allow the testRouteAddition junit to complete
echo "Going to sleep for some time"
sleep 10
echo "Woke up from sleep.By now, route additions should be done!"
chmod 666 playbook-run-tests-results.yml ansible_inventory
ansible-playbook playbook-run-tests-results.yml -i ansible_inventory

#sleep for some time to allow the testRouteUnreachable junit to complete
echo "Going to sleep for some time"
sleep 240
echo "Woke up from sleep.By now, routes should have become unreachable"
chmod 666 playbook-run-tests-results.yml ansible_inventory
#ansible-playbook --limit '!hoost1:!host2' playbook-run-tests-results.yml -i ansible_inventory
ansible-playbook --limit '!192.168.10.11' playbook-run-tests-results.yml -i ansible_inventory

#sleep for some time to allow the testRouteDelete junit to complete
echo "Going to sleep for some time"
sleep 520
echo "Woke up from sleep.By now, unreachable routes should have got deleted"
chmod 666 playbook-run-tests-results.yml ansible_inventory
#ansible-playbook --limit '!hoost1:!host2' playbook-run-tests-results.yml -i ansible_inventory
ansible-playbook --limit '!192.168.10.11' playbook-run-tests-results.yml -i ansible_inventory


