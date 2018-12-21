package com.busekylin.springresttemplate.web;

import com.busekylin.springresttemplate.entity.Inner;
import com.busekylin.springresttemplate.entity.Template;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class WebController {
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private static final String BASE_URL = "http://localhost:8080";

    /**
     * get请求返回一个对象
     */
    @GetMapping("/get")
    public void restTemplateGet() {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(BASE_URL + "/test/get")
                .queryParam("type", "type")
                .queryParam("version", "1.0");

        Template template = restTemplate.getForObject(
                builder.toUriString(),
                Template.class);

        System.out.println(template);
    }

    /**
     * get请求返回一个list对象
     *
     * @return
     */
    @GetMapping("/get/list")
    public List<Inner> restTemplateGetList() {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(BASE_URL + "/test/list");

        ResponseEntity<List<Inner>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Inner>>() {
                }
        );

        return response.getBody();
    }

    /**
     * 请求体为json的post请求(类的方式)
     */
    @GetMapping("/post/json")
    public void restTemplatePostJson() {
        /* 设置请求头 */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        /* 设置请求体和内容合并的RequestEntity */
        HttpEntity<Template> entity = new HttpEntity<>(templateGen(), headers);

        /* 返回的响应体 */
        ResponseEntity<Inner> response = restTemplate.postForEntity(BASE_URL + "/test/post/json",
                entity, Inner.class);

        System.out.println(response.getBody());
    }

    /**
     * 请求体为json的post请求(ObjectMapper拼接的方式)
     */
    @GetMapping("/post/json/inner")
    public void restTemplatePostJsonInner() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        ObjectNode node = mapper.createObjectNode();
        node.put("name", "中文");
        node.put("age", 22);

        HttpEntity<String> entity = new HttpEntity<>(node.toString(), headers);

        /* 返回的响应体 */
        String response = restTemplate.postForObject(BASE_URL + "/test/post/json/inner",
                entity, String.class);

        JsonNode jsonNode = mapper.readTree(response);

        System.out.println(jsonNode.get("age").asInt());
    }

    /**
     * 请求体为x-www-form-urlencoded的post请求
     */
    @GetMapping("/post/urlencoded")
    public void restTemplatePostUrlencoded() {
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("inner.name", "inner");
        form.add("type", "type1");
        form.add("version", "3.0");
        form.add("list", "1,4,6");

        Inner inner = restTemplate.postForObject(BASE_URL + "/test/post/urlencoded",
                form, Inner.class);

        System.out.println(inner);

    }

    private Template templateGen() {
        Template template = new Template();
        template.setType("版本");
        template.setVersion("1.0");
        template.setLists(Arrays.asList("list1", "list2"));

        Inner inner = new Inner();
        inner.setName("中文");
        inner.setAge(12);
        template.setInner(inner);

        return template;
    }

    @PostMapping("/test/post/urlencoded")
    public Inner testPostUrlencoded(Template template) {
        System.out.println(template);
        return template.getInner();
    }

    @PostMapping("/test/post/json")
    public Inner testPostJson(@RequestBody Template template) {
        System.out.println(template.getInner().getName());
        return template.getInner();
    }

    @PostMapping("/test/post/json/inner")
    public Inner testPostJsonInner(@RequestBody Inner inner) {
        System.out.println(inner);
        return inner;
    }

    @GetMapping("/test/get")
    public Template testGet(String type, String version) {
        Template template = new Template();
        template.setType(type);
        template.setVersion(version);
        template.setLists(Arrays.asList("list1", "list2"));

        Inner inner = new Inner();
        inner.setName("inner");
        template.setInner(inner);

        return template;
    }

    @GetMapping("/test/list")
    public List<Inner> testList() {
        Inner i1 = new Inner("name1", 12);
        Inner i2 = new Inner("name2", 13);

        return Arrays.asList(i1, i2);
    }
}
