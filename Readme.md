
docker build -t hatimmatik:1.0.16 -t hatimmatik:latest .

docker service create -d -p 8081:5050   --name hatimmatik hatimmatik:1.0.1  

docker service update --image hatimmatik:1.0.4 hatimmatik 



development vm options
--app.securityEnabled=true --app.redisServerHost=127.0.0.1 --spring.datasource.url=jdbc:postgresql://localhost:5432/hm --spring.datasource.password=postgres

redis için server bağlantısı yada local redis olmalı
ssh -L 6379:85.117.239.232:6379 root@85.117.239.232
