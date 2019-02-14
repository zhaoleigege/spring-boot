# Spring boot使用gatling进行压力测试

## pom依赖

```java
<dependencies>
...
	<dependency>
		<groupId>io.gatling.highcharts</groupId>
		<artifactId>gatling-charts-highcharts</artifactId>
		<version>3.0.2</version>
		<scope>test</scope>
	</dependency>
</dependencies>
...
    
<build>
	<plugins>
		<!-- spring boot需要的maven插件 -->
		<plugin>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-maven-plugin</artifactId>
		</plugin>
		
		<!-- 以下为gatling需要的maven插件 -->
		<plugin>
			<groupId>net.alchim31.maven</groupId>
			<artifactId>scala-maven-plugin</artifactId>
			<version>3.3.1</version>
 		</plugin>
		<plugin>
			<groupId>io.gatling</groupId>
			<artifactId>gatling-maven-plugin</artifactId>
			<version>3.0.1</version>
		</plugin>
	</plugins>
</build>
```



## 编写压力测试代码

在src/test目录下新建类型为`Test Source Root` 的文件夹`scala`，在里面书写[gatling](https://gatling.io/docs/3.0/)测试文件

```scala
package pivotal

import io.gatling.core.Predef._
import io.gatling.http.Predef._

/* 所有压力测试类都要继承Simulation类 */
class PerformanceSimulation extends Simulation {

  /* 具体方法使用规则参考官网 */
  val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("text/plain")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Gatling")

  val scn = scenario("PerformanceSimulation")
    .repeat(10) {
      exec(http("GET /io").get("/io"))
    }

  setUp(
    scn.inject(atOnceUsers(1000))
  ).protocols(httpProtocol)
}
```



## 压力测试命令

```shell
# 启动spring boot项目
mvn spring-boot:run
# 另外一个终端启动gatling测试
mvn gatling:test
# 查看测试报告
open target/gatling/performancesimulation-*/index.html
```



#### 参考资料

* [spring-boot-load-testing](https://github.com/geoffwa/spring-boot-load-testing)