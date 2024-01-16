#! /bin/bash
#mount /dev/nvme0n1p3 /mnt
#systemctl start docker.service
docker start mariadb0
docker start flow1000-server
docker start nginx

#mount /dev/sda1 /mnt/2048

