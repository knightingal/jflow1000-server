# How to build docker image

```
docker buildx build -t flow1000:x.x -f Dockerfile .
```

# How to run docker image

cp `application.properties` into /home/knightingal

set `apk.filepath.base` as `/usr/share/nginx/html/`, and set `apk.filepath.aapt.path` as `/opt/app/aapt2`

```
docker network create db-network

docker run -d --network db-network --name mariadb0 --env MARIADB_ROOT_PASSWORD=000000 -v /var/lib/mysql:/var/lib/mysql -p 3306:3306 mariadb:latest

docker run -d  --network db-network -v /usr/share/nginx/html:/usr/share/nginx/html -v /home/knightingal/application.properties:/opt/app/config/application.properties -p 8000:8000 --name flow1000-server  flow1000:x.x

docker run -d -p 3002:3002 -p 3001:3001 -p 3003:3003 --name nginx --network db-network -v /usr/share/nginx/html:/usr/share/nginx/html -v /home/knightingal/source/md-client/build:/usr/share/nginx/flow1000-front -v /home/knightingal/source/mp4_viewer_client/build/web:/usr/share/nginx/flutter-web -v /mnt:/mnt -v /etc/nginx/nginx.conf:/etc/nginx/nginx.conf nginx
```

# Auto mount drive

* find uuid of drive `/dev/nvme0n1p3`

```
[root@192 ~]# blkid
/dev/nvme0n1p7: UUID="51769f2a-25b8-4a96-9b7f-430e0bf133eb" BLOCK_SIZE="4096" TYPE="ext4" PARTUUID="6feee6eb-2eea-4540-b4f2-24940282628a"
/dev/nvme0n1p5: SEC_TYPE="msdos" LABEL_FATBOOT="boot" LABEL="boot" UUID="1066-A557" BLOCK_SIZE="512" TYPE="vfat" PARTLABEL="EFI System Partition" PARTUUID="02f8b62f-27b0-4ed4-89d4-c1e25f7ff0c1"
/dev/nvme0n1p3: BLOCK_SIZE="512" UUID="BEB27BE7B27BA317" TYPE="ntfs" PARTLABEL="Basic data partition" PARTUUID="147da59b-4fc1-4942-b634-f641555200fd"
/dev/nvme0n1p1: UUID="AC71-60A0" BLOCK_SIZE="512" TYPE="vfat" PARTLABEL="EFI system partition" PARTUUID="938c0000-3e78-44f4-ae52-cc966a00e39a"
/dev/nvme0n1p6: UUID="cda8409d-3cea-443d-951e-94f6cf685227" TYPE="swap" PARTUUID="a253ebb6-1722-4917-b9d9-772133545ebf"
/dev/nvme0n1p4: BLOCK_SIZE="512" UUID="5022B7C922B7B1F8" TYPE="ntfs" PARTUUID="67b38c96-2ae9-4b49-8357-a918f0a7bfda"
/dev/nvme0n1p2: PARTLABEL="Microsoft reserved partition" PARTUUID="7dbd01a8-52ec-4f31-9637-1d90902efa90"
/dev/sda2: BLOCK_SIZE="512" UUID="5EDEA04EDEA0206F" TYPE="ntfs" PARTUUID="067ce025-02"
/dev/sda3: BLOCK_SIZE="512" UUID="824417E04417D62F" TYPE="ntfs" PARTUUID="067ce025-03"
/dev/sda1: LABEL="M-gM-3M-;M-gM-;M-^_M-dM-?M-^]M-gM-^UM-^Y" BLOCK_SIZE="512" UUID="BA489B2E489AE901" TYPE="ntfs" PARTUUID="067ce025-01"
/dev/zram0: LABEL="zram0" UUID="0caa0455-ef72-43c0-9654-812fcd539a01" TYPE="swap"
```

* edit `/etc/fstab`
```
vi /etc/fstab
UUID=61163017-457c-4c46-88ef-36e437f02b53 /                       ext4    defaults        1 1
UUID=2225-2C9C          /boot/efi               vfat    umask=0077,shortname=winnt 0 2
UUID=e1c05eae-4066-44de-a396-3cebbdd0433d none                    swap    defaults        0 0
UUID=E00A5E8C0A5E6018                      /mnt               ntfs      defaults,nofail,user,rw 0 2
UUID=007E-40C8                             /mnt/drive3        exfat     defaults,nofail,user,rw 0 0
UUID=AD97-0C20                             /mnt/drive2        exfat     defaults,nofail,user,rw 0 0
UUID=500485B00485999C                      /mnt/drive1        ntfs      defaults,nofail,user,rw 0 0
UUID=824417E04417D62F                      /mnt/迅雷下载/tmp2  ntfs      defaults,nofail,user,rw 0 0
```

# Flow1000 service config

## Enable docker service
```
systemctl enable docker.service
```

## Config flow1000 service
put flow1000.service into /etc/systemd/system/

put start_flow1000.sh and stop_flow1000.sh into /root

close selinux
```
chmod +x /root -R

systemctl enable flow1000.service

systemctl status flow1000.service
```

## For fedora permanet wifi address

1. Create a custom configuration file in `/etc/NetworkManager/conf.d/22-wifi-mac-addr.conf`, which can be empty or contain specific configurations. This will prevent the default file in /usr/lib from being loaded. 
2. Create a higher priority .conf file like `/etc/NetworkManager/conf.d/90-wifi-mac-addr.conf` with: 
```
[connection-90-wifi-mac-addr-conf]
wifi.cloned-mac-address=permanent
```

## For issue in openSUSE when IDEA start up

```shell
sudo zypper install libgthread-2_0-0
```

## Fix ntfs in arch linux

```shell
sudo pacman -S nfs-utils
sudo pacman -S ntfs-3g
sudo ntfsfix /dev/nvme0n1p3
sudo mount -a
```