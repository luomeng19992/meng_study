#!/usr/bin/env bash
app_name='meng'
docker stop ${app_name}
echo '----stop container----'
docker rm ${app_name}
echo '----rm container----'
docker run -p 8088:8088 --name ${app_name} \
--link mysql:db \
--link elasticsearch:es \
--link redis:rd \
-v /etc/localtime:/etc/localtime \
-v /mydata/app/${app_name}/logs:/var/logs \
-d luomeng/${app_name}:1.0-SNAPSHOT
echo '----start container----'
