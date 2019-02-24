# spring boot启动方式

1. 运行java主类

   ```shell
   # 因为spring boot项目包含了main函数，开发时直接启动main函数即可运行
   ```

2. `maven`插件运行

   ```xml
   <!-- 添加了spring boot maven插件后可以执行maven命令运行 -->
   <build>
   	<plugins>
   		<plugin>
   			<groupId>org.springframework.boot</groupId>
   			<artifactId>spring-boot-maven-plugin</artifactId>
   		</plugin>
   	</plugins>
   </build>
   ```

   运行命令

   ```shell
   mvn spring-boot:run
   ```

3. 打包成`jar`包运行

   生成`jar`包命令

   ```shell
   mvn clean package
   ```

   * 直接jar包运行

     ```shell
     java -Xlog:gc* -Xlog:gc:/home/log/gc.log -jar target/springboot-0.0.1-SNAPSHOT.jar --server.port=8000
     ```

   * 后台运行jar包

     ```shell
     nohup java -Xlog:gc* -Xlog:gc:/home/log/gc.log -jar target/springboot-0.0.1-SNAPSHOT.jar --server.port=8000 &
     ```

   * 作为系统服务运行

     添加执行参数

     ```xml
     <build>
     	<plugins>
     		<plugin>
     		<groupId>org.springframework.boot</groupId>
     		<artifactId>spring-boot-maven-plugin</artifactId>
     		<configuration>
     			<executable>true</executable>
     		</configuration>
     		</plugin>
     	</plugins>
     </build>
     ```

     ```shell
     mvn clean package # 编译
     target/springboot-0.0.1-SNAPSHOT.jar start # 运行
     ```

4. [docker启动](./springboot-docker)

#### 参考资料

* [Spring Boot应用的后台运行配置](http://blog.didispace.com/spring-boot-run-backend/)

