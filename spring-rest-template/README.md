# RestTemplate的使用

1. get请求

   ```java
   UriComponentsBuilder builder = UriComponentsBuilder
                   .fromUriString(BASE_URL + "/test/get")
                   .queryParam("type", "type")
                   .queryParam("version", "1.0");
   
   Template template = restTemplate.getForObject(
                   builder.toUriString(),
                   Template.class);
   ```

2. post请求

   * 请求格式为application/json

     ```java
     Template template = new Template();
     template.setType("版本");
     template.setVersion("1.0");
     template.setLists(Arrays.asList("list1", "list2"));
     
     Inner inner = new Inner();
     inner.setName("中文");
     inner.setAge(12);
     template.setInner(inner);
     
     /* 设置请求头 */
     HttpHeaders headers = new HttpHeaders();
     headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
         
     /* 设置请求体和内容合并的RequestEntity */
     HttpEntity<Template> entity = new HttpEntity<>(template, headers);
         
     /* 返回的响应体 */
     ResponseEntity<Inner> response = restTemplate.postForEntity(
     	BASE_URL + "/test/post/json",
         entity, 
         Inner.class);
     
     System.out.println(response.getBody());
     ```

   * 请求格式为application/x-www-form-urlencoded和multipart/form-data

     ```java
     MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
     form.add("inner.name", "inner");
     form.add("type", "type1");
     form.add("version", "3.0");
     form.add("list", "1,4,6");
     
     // 如果上传文件
     // form.add("file", new ClassPathResource("myFile.jpg"));
     
     Inner inner = restTemplate.postForObject(
         BASE_URL + "/test/post/urlencoded",
         form, 
         Inner.class);
     
     System.out.println(inner);
     ```

3. 返回结果为List的情况

   ```java
   UriComponentsBuilder builder = UriComponentsBuilder
                   .fromUriString(BASE_URL + "/test/list");
   
   ResponseEntity<List<Inner>> response = restTemplate.exchange(
   	builder.toUriString(),
   	HttpMethod.GET,
   	null,
   	new ParameterizedTypeReference<List<Inner>>() {});
   
   return response.getBody();
   ```

4. 不想建一个类却想直接获得其中一两个属性的方法

   ```java
   HttpHeaders headers = new HttpHeaders();
   headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
   
   ObjectNode node = mapper.createObjectNode();
   node.put("name", "中文");
   node.put("age", 22);
   
   HttpEntity<String> entity = new HttpEntity<>(node.toString(), headers);
   
   /* 返回的响应体 */
   String response = restTemplate.postForObject(
       BASE_URL + "/test/post/json/inner",
       entity, 
       String.class);
   
   JsonNode jsonNode = mapper.readTree(response);
   
   System.out.println(jsonNode.get("age").asInt());
   ```


#### 参考资料
* [返回为list的对象](https://www.baeldung.com/spring-rest-template-list)
* [在spring boot使用resttemplate](https://www.oodlestechnologies.com/blogs/Learn-To-Make-REST-calls-With-RestTemplate-In-Spring-Boot)
* [Jackson 框架的高阶应用](https://www.ibm.com/developerworks/cn/java/jackson-advanced-application/index.html)