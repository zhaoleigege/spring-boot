# spring boot集群搭建

搭建spring boot集群的问题在于session如何保持同步的问题，在这里我们使用redis作为第三方存储来外置session。

1. 安装并启动redis

   ```shell
   docker pull redis # 下载redis镜像
   
   docker run --name redis -v .../redis/data:/data -p 6379:6379 -d redis redis-server --appendonly yes --requirepass "redis" # 启动redis并设置本机存储redis的数据和给redis设置密码
   ```

2. 引入`redis-session`依赖

   ```xml
   <!-- 引入redis session -->
   <dependency>
   	<groupId>org.springframework.session</groupId>
   	<artifactId>spring-session-data-redis</artifactId>
   </dependency>
   <dependency>
   	<groupId>org.springframework.boot</groupId>
   	<artifactId>spring-boot-starter-data-redis</artifactId>
   </dependency>
   ```

3. 配置开启redis存储session

   ```java
   @EnableRedisHttpSession
   public class RedisSessionConfig {
       /**
        * 设置按照json格式存储session中的内容
        * @return
        */
       @Bean
       public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
   }
   ```

4. 配置开发环境的`properties`文件

   * application-dev.properties

     ```properties
     server.port=8000
        
     spring.redis.host=localhost
     spring.redis.port=6379
     spring.redis.password=redis
        
     serverID=${SERVER_ID:dev}
     ```

   * application.properties

     ```properties
     spring.profiles.active=dev
     spring.application.name=spring boot集群
     ```

5. 启动项目

   ```shell
   # 先确保已经启动redis
   
   # 启动spring boot项目
   mvn spring-boot:run
   ```

6. 查看是否启动成功

   ```shell
   curl http://localhost:8000/info
   ```

7. 配置docker集群

   因为上面已经验证了我们程序的正确性，这里我们就用`docker`来模拟集群模式，其中使用`nginx`做负载均衡。

   在项目中添加`application-prod.properties`文件

   ```properties
   serverID=${SERVER_ID}
   ```

   在项目中添加`Dockerfile`文件，使得项目可以打包成`docker镜像`

   ```dockerfile
   FROM openjdk:8-jdk-alpine
   
   VOLUME /tmp
   
   ARG JAR_FILE
   COPY ${JAR_FILE} app.jar
   
   ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
   ```

    在`pom.xml`文件中添加生成镜像的插件

   ```xml
    <properties>
           <docker.image.prefix>buse</docker.image.prefix>
           <docker.image.repository>springboot-cluster</docker.image.repository>        			<dockerfile-maven-plugin.version>1.4.9</dockerfile-maven-plugin.version>
   </properties>
   ...
   
   <!-- 打包成docker项目 -->
   <plugin>
   	<groupId>com.spotify</groupId>
   	<artifactId>dockerfile-maven-plugin</artifactId>
   	<version>${dockerfile-maven-plugin.version}</version>
   	<configuration>
           <repository>
   			${docker.image.prefix}/${docker.image.repository}
   		</repository>
   		<buildArgs>
   			<JAR_FILE>target/${project.build.finalName}.jar</JAR_FILE>
   		</buildArgs>
   		</configuration>
   </plugin>
   ```

   执行命令生成`docker镜像`

   ```shell
   mvn install dockerfile:build -Dmaven.test.skip=true # 跳过测试代码
   ```

   创建目录`docker-compose`，并编写`docker-compose.yml`文件

   ```dockerfile
   version: '3'
   
   services:
     nginx: # 配置nginx依赖
       image: nginx:alpine
       ports:
         - "80:80"
       networks:
         - redis-cluster
       volumes:
         - ./nginx.conf:/etc/nginx/conf.d/default.conf
       depends_on:
         - cluster1
         - cluster2
   
     cluster1: # 第一个spring boot镜像
       image: buse/springboot-cluster
       networks:
         - redis-cluster
       environment: # 配置spring boot的环境变量
         SPRING_REDIS_HOST: redis-docker
         SPRING_REDIS_PASSWORD: dockerRedis
         SPRING_REDIS_PORT: 6379
         SERVER_ID: cluster1
         SPRING_PROFILES_ACTIVE: prod
       depends_on:
         - redis-docker
   
     cluster2: # 第二个spring boot镜像
       image: buse/springboot-cluster
       networks:
         - redis-cluster
       environment: # 配置spring boot的环境变量
         SPRING_REDIS_HOST: redis-docker
         SPRING_REDIS_PASSWORD: dockerRedis
         SPRING_REDIS_PORT: 6379
         SERVER_ID: cluster2
         SPRING_PROFILES_ACTIVE: prod
       depends_on:
         - redis-docker
   
     redis-docker: # 配置redis
       image: redis
       volumes:
         - /Users/buse/Downloads/redis/data:/data
       entrypoint: redis-server --appendonly yes --requirepass "dockerRedis"
       networks:
         - redis-cluster
       ports:
         - "6379:6379"
   
   networks: # 创建此集群使用的网络
     redis-cluster:
   ```

   配置`nginx.conf`文件

   ```conf
   upstream cluster {
     server cluster1:8080;
     server cluster2:8080;
   }
   
   server {
     listen       80;
   
     location / {
       proxy_pass http://cluster;
     }
   }
   ```

   转到`docker-compose`目录

   **可选的命令**

   ```shell
   docker rm -f $(docker ps -a | awk '{print $1}') # 删除所有在运行的容器
   docker rmi -f $(docker images | awk '{print $3}') # 删除所有的镜像
   ```

   执行命令

   ```shell
   docker-compose up -d # 后台启动
   
   docker ps -a # 查看运行的容器id
   docker logs 04f # 查看运行日志
   docker exec -it 04f /bin/sh # 进入该容器的命令行
   
   docker-compose stop # 停止所有的容器
   ```

   测试

   ```shell
   # 访问浏览器http://localhost/info 会发现sessionID一样，但是serverID不一样，
   # 这说明我们的集群访问了redis库来存取session但是处理的后端服务器不一样
   ```

8. 配置redis缓存

   1. 添加依赖

      `pom.xml`

      ```xml
      <dependency>
      	<groupId>org.springframework.boot</groupId>
      	<artifactId>spring-boot-starter-cache</artifactId>
      </dependency>
      ```

   2. 设置缓存管理器

      ```java
      @Configuration
      @EnableCaching
      public class RedisCacheConfig {
      
          /**
           * 自定义缓存管理器
           * @param redisConnectionFactory
           * @return
           */
          @Bean
          public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
              return RedisCacheManager.builder(redisConnectionFactory)
                      .cacheDefaults(
                              RedisCacheConfiguration.defaultCacheConfig()
                                      .disableCachingNullValues()
                                      .entryTtl(Duration.ofMinutes(30))
                                      .serializeValuesWith(RedisSerializationContext.SerializationPair
                                              /* 设置数据以json的方式缓存在redis中 */
                                              .fromSerializer(new GenericJackson2JsonRedisSerializer()))
                      )
                      .build();
          }
      }
      ```

   3. 添加缓存注解

      ```java
      @Repository
      @Slf4j
      @CacheConfig(cacheNames = {"person"})
      public class DefaultPersonRepository implements PersonRepository {
          public static final String EMPTY_KEY = "redis-key";
      
          @Cacheable(key = "#root.target.EMPTY_KEY")
          @Override
          public Person getPerson() {
              log.info("生成person对象");
              return generatorPerson();
          }
      
          @Cacheable(key = "#size")
          @Override
          public List<Person> getPersonList(int size) {
              log.info("生成personList对象");
              return Stream.generate(this::generatorPerson).limit(size).collect(Collectors.toList());
          }
      
          private Person generatorPerson() {
              Person person = new Person();
              person.setName(randomString(5, 10));
              person.setAge((int) (70 * Math.random()));
              person.setAddress(randomString(20, 30));
      
              return person;
          }
      
          private String randomString(int min, int max) {
              String str = "qazxswedcvfrtgbnhyujmkiolp098762143";
      
              StringBuilder builder = new StringBuilder();
              for (int i = 0; i < (int) (Math.random() * max); i++) {
                  builder.append(str.charAt((int) (Math.random() * str.length())));
              }
      
      
              return builder.toString();
          }
      
      }
      ```

      **注意：`cacheNames`是一定需要的，可以在`@CacheConfig`中统一配置，或者在`@Cacheable`中指明**

   4. 测试代码的编写

      如果测试`controller`时需要一起验证`service`和`repository`的正确性，则需要添加`@SpringBootTest`和`@AutoConfigureMockMvc`注解

      ```java
      @RunWith(SpringRunner.class)
      @SpringBootTest
      @AutoConfigureMockMvc
      public class SpringbootClusterApplicationTests {
      
          @Autowired
          private MockMvc mockMvc;
      
          @Test
          public void personTest() throws Exception {
              this.mockMvc.perform(get("/person")).andDo(print());
          }
      
          @Test
          public void personsTest() throws Exception {
              this.mockMvc.perform(get("/persons/4")).andDo(print());
          }
      }
      ```

      

#### 参考资料

* [spring boot配置profiles](https://dzone.com/articles/spring-boot-profiles-1)
* [Spring Session官方文档](https://docs.spring.io/spring-session/docs/current/reference/html5/)  **推荐**
* [Spring Session 实现分布式会话管理 ](https://my.oschina.net/langxSpirit/blog/872029)
* [spring cluster example](https://github.com/aetzlstorfer/spring-boot-cluster-example)
* [spring 缓存](https://spring.io/guides/gs/caching/)  **推荐**