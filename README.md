# Arachne
Arachne is a Java-based routing library. This runs on simple Linux VMs, and makes such VMs to take on the role of a sophosticated router.As of now, it has implementation for RIPv2 protocol over UDP.

## Set-up steps
### VMs set up
This library has been tested on Ubuntu VMs running over Oracle Virtual Box.A combination of Vagrant and Ansible has been used to install/configure the VMs.
1. On the host machine, Oracle Virtual Box and Vagrant should be present.
2. Add the Vagrant Box (representing ubuntu/trusty64 OS image) to the host machine with command - `vagrant box add ubuntu/trusty64`
3. To install the VMs, go to setup/vagrant dircetory and run - `vagrant up`
4. 
