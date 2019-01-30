# 使用docker部署spring boot项目

### 打包运行一个完整的项目

1. 添加maven插件

   ```java
   <plugin>
   	<groupId>com.spotify</groupId>
   	<artifactId>dockerfile-maven-plugin</artifactId>
   	<version>${docker-maven-version}</version>
   	<configuration>
   		<repository>
   			${docker.image.prefix}/${project.artifactId}
   		</repository>
   		<tag>${project.version}</tag>
   		<buildArgs>
   			<JAR_FILE>target/${project.build.finalName}.jar</JAR_FILE>
   		</buildArgs>
   	</configuration>
   </plugin>
   ```

2. 创建`Dockerfile`文件

   ````dockerfile
   # 在项目根目录下创建文件Dockerfile
   FROM openjdk:8-jdk-alpine
   VOLUME /tmp
   ARG JAR_FILE
   COPY ${JAR_FILE} app.jar
   ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
   ````

3. 执行编译命令

   ```shell
    mvn install dockerfile:buil
   ```

4. 运行docker容器

   ```shell
   docker run --rm --name springboot -p 8080:8080 buse/springboot-docker:1.0
   ```

<hr></hr>

### 动态指定运行时的环境变量

1. 如果需要定义`application.properties`文件中的变量

   ```properties
   # application.properties
   # 环境变量的变量名只能是大写
   name=${NAME:test}
   age=${AGE:12}
   ```

   在`Component`类中使用

   ```java
   @Value("${name}")
   private String name;
   
   @Value("${age}")
   private int age;
   ```

2. 执行同样的编译命令

3. 运行docker容器

   ```shell
   docker run --rm --name springboot -p 8080:8080 -e NAME=test -e AGE=10 buse/springboot-docker:1.0
   ```

<hr></hr>

### 动态指定`application.properties`文件

1. 添加运行脚本文件`start.sh`

   ```shell
   #!/bin/bash
   set -e
   
   # 如果在容器的/config目录中存在application.properties文件则启动spring时指定使用该配置文件，否则使用项目默认的配置文件
   if [[ -f /config/application.properties ]]
   then
       java -Djava.security.egd=file:/dev/./urandom -Dspring.config.location=/config/application.properties -jar /app.jar
   else
       java -Djava.security.egd=file:/dev/./urandom -jar /app.jar
   fi
   ```

2. 修改`Dockerfile`文件

   ```shell
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
   ```

3. 执行编译命令

   ```shell
   mvn install dockerfile:build
   ```

4. 运行docker容器

   * 使用默认的`application.properties`文件

     ```shell
     docker run --rm --name springboot -p 8080:8080 -e NAME=test -e AGE=10 buse/springboot-docker:1.0
     ```

   * 使用自定义的`application.properties`文件(~/Desktop/config/目录中包含了application.properties文件)

     ```shell
     docker run --rm --name springboot -p 8080:8080 -v ~/Desktop/config/:/config buse/springboot-docker:1.0
     ```




#### 参考资料

* [docker maven官网](https://github.com/spotify/dockerfile-maven)
* [Using Docker containers for your Spring boot applications](https://g00glen00b.be/docker-spring-boot/)
* [Configuration as Code With Docker and Spring Boot](https://dzone.com/articles/implementing-configuration-as-code-with-docker-and)

