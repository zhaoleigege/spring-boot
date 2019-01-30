FROM openjdk:8-jdk-alpine

VOLUME /tmp
# 新添加一个管理配置文件的容器卷
VOLUME /config

ARG JAR_FILE
COPY ${JAR_FILE} app.jar

# 添加启动脚本到docker中，并赋予可执行的权限
ADD start.sh start.sh
RUN apk add --no-cache bash && \
    bash -c 'chmod +x /start.sh'

# 启动命令为执行start.sh脚本
ENTRYPOINT ["/bin/bash", "/start.sh"]