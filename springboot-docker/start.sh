#!/bin/bash
set -e

# 如果在容器的/config目录中存在application.properties文件则启动spring时指定使用该配置文件，否则使用项目默认的配置文件
if [[ -f /config/application.properties ]]
then
    java -Djava.security.egd=file:/dev/./urandom -Dspring.config.location=/config/application.properties -jar /app.jar
else
    java -Djava.security.egd=file:/dev/./urandom -jar /app.jar
fi