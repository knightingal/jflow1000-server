# How to build docker image

```
docker buildx build -t flow1000:x.x -f Dockerfile .
```

# How to run docker image
```
docker run -d  --network db-network -v /usr/share/nginx/html:/usr/share/nginx/html -v /home/knightingal/application.properties:/opt/app/config/application.properties -p 8000:8000 --name flow1000-server  flow1000:x.x

docker run -d -p 3002:3002 -p 3001:3001 -p 3003:3003 --name nginx --network db-network -v /usr/share/nginx/html:/usr/share/nginx/html -v /home/knightingal/source/md-client/build:/usr/share/nginx/flow1000-front -v /usr/share/nginx/flutter-web:/usr/share/nginx/flutter-web -v /mnt:/mnt -v /etc/nginx/nginx.conf:/etc/nginx/nginx.conf nginx
```

# Flow1000 service config

put flow1000.service into /etc/systemd/system/

put start_flow1000.sh and stop_flow1000.sh into /root

```
chmod +x /root -R
```

```
systemctl status flow1000.service
```