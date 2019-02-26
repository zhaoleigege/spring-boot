# Spring boot测试

spring boot项目需要书写明确和清晰的测试用例，spring boot也提供了专门的测试starter。

1. 导入相关依赖

   `pom.xml`

   ```xml
   <dependency>
   	<groupId>org.springframework.boot</groupId>
   	<artifactId>spring-boot-starter-test</artifactId>
   	<scope>test</scope>
   </dependency>
   ```

2. 只测试`controller`

   ```java
   @RunWith(SpringRunner.class)
   @WebMvcTest(WebController.class)
   public class ControllerTest {
       @Autowired
       private MockMvc mockMvc;
   
       @MockBean
       private WebService webService;
   
       @Test
       public void controllerTest() throws Exception {
           when(webService.getWeb()).thenReturn(new Web("http://127.0.0.1", 8080));
           this.mockMvc.perform(get("/web")).andDo(print()).andExpect(status().isOk());
       }
   
   }
   ```

   其中`@mockBean`只能用来模拟本项目自己写的类的返回值，如果需要模拟导入的类的返回值需要使用`@SpyBean`。

3. 只测试`service`

   ```java
   @RunWith(SpringRunner.class)
   @ContextConfiguration(classes = {DefaultWebServiceImpl.class})
   public class ServiceTest {
       @Autowired
       private WebService webService;
   
       @MockBean
       private WebRepository webRepository;
   
       @Test
       public void serviceTest() {
           when(webRepository.getWeb()).thenReturn(new Web("http://localhost", 8080));
           System.out.println(webService.getWeb());
       }
   }
   ```

   其中`@ContextConfiguration`制定了接口的具体实现。

4. 整个项目测试完`controller`、`service`和`repository`

   ```java
   @RunWith(SpringRunner.class)
   @SpringBootTest
   @AutoConfigureMockMvc
   public class ContextTest {
       @Autowired
       private MockMvc mockMvc;
   
       @Test
       public void contextTest() throws Exception {
           this.mockMvc.perform(get("/web")).andDo(print()).andExpect(status().isOk());
       }
   }
   ```

#### 参考资料

* [使用 @MockBean 和 @SpyBean 解决 SpringBoot 单元测试中 Mock 类装配的问题](https://sspai.com/post/48245)
* [https://sspai.com/post/48245](https://shekhargulati.com/2017/07/20/using-spring-boot-spybean/)
* [Spring Boot Test: Writing Unit Tests for the Controller Layers with @WebMvcTest](https://springbootdev.com/2018/02/22/spring-boot-test-writing-unit-tests-for-the-controller-layers-with-webmvctest/)
* [Testing the Web Layer](https://spring.io/guides/gs/testing-web/)  **推荐**

