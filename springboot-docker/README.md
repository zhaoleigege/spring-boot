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
   docker run --rm --name springboot -p 8080:8080 -e NAME=赵磊 -e AGE=23 buse/springboot-docker:1.0
   ```

<hr></hr>

### 动态指定`application.properties`文件

