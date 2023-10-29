# How to build docker image

```
docker buildx build -t flow1000:x.x -f Dockerfile .
```

# How to run docker image
```
docker run -d  --network db-network  -v /home/knightingal/application.properties:/opt/app/config/application.properties -p 8000:8000 --name flow1000-server  flow1000:x.x
```