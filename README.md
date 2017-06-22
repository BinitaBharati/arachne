# Arachne
Arachne is a Java-based routing library. This runs on simple Linux VMs, and makes such VMs to take on the role of a sophosticated router.As of now, it has implementation for RIPv2 protocol over UDP.

## Set-up steps
### Build
The project has two sub projects `arachne.core` and `arachne.test`. `mvn clean package` will generate the artfifact jars for both core and test sub projects.
### Sample network diagram
![Alt text](docs/ND.png "Network Diagram") <br />

### VMs set up
This library has been tested on Ubuntu VMs running over Oracle Virtual Box.A combination of Vagrant and Ansible has been used to install/configure the VMs.
1. On the host machine, Oracle Virtual Box and Vagrant should be present.
2. Minimum of 16 GB RAM is required on the host machine for optimal VM performance wrt sample configuration.
3. Add the Vagrant Box (representing ubuntu/trusty64 OS image) to the host machine with command - `vagrant box add ubuntu/trusty64`
4. To install the VMs, go to setup/vagrant directory and run - `vagrant up`. The sample VM configuration will set up a total of 8 VMs, with one of the VMs being the workstation VM. Here-onwards, all the other VM configurations are done through the workstation VM.

### Run Java routing service
1. ssh to the workstation VM with default `vagrant` user credentials.
2. Copy the `arachne.core` and `arachne.test` artifact jars from the respective build folder to `~/deploy/target`
3. Execute script to bring up the Routing Java service on all other VMs. `~/deploy/target/scripts/runInstall.sh` 


### Verify route entries
1. After succesful run of the `runInstall.sh` script, we should see dynamically discovered route getting automatically added to the respective VMs.
2. Junit has been used to run the test cases via Ansible on all the VMs.
3. ssh to the workstation VM with default `vagrant` user credentials.
4. Execute script to verify the route entries.  `~/deploy/target/scripts/runTests.sh`.
5. Attached below is the route table of `ROUTER2` with dynamically added routes highlighted in red.
![Alt text](docs/rt1.png "Route table") <br />