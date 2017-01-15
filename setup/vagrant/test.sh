#!/bin/sh
 
# concurrency is hard, let's have a beer
 
MAX_PROCS=4
 
parallel_provision() {
    while read box; do
        echo "Provisioning '$box'. Output will be in: $box.out.txt" 1>&2
        echo $box
    done | xargs -P $MAX_PROCS -I"BOXNAME" \
        sudo sh -c 'vagrant provision BOXNAME >BOXNAME.out.txt 2>&1 || echo "Error Occurred: BOXNAME"'
}
 
## -- main -- ##
 
# start boxes sequentially to avoid VirtualBox kernel explosions
sudo vagrant up --no-provision
 
# but run provision tasks in parallel
cat <<EOF | parallel_provision
net1mc1
net1mc2
router1
net4mc1
net2mc1
net2mc2
router2
router3
net3mc1
EOF