package com.busekylin.springbootexception.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class WebController {
    
    @GetMapping("person")
    public Person getPerson() {
        log.debug("debug person请求");
        log.info("person请求");
        log.warn("warn person请求");
        return new Person("test", 21);
    }

    @GetMapping("error-page")
    public void error() {
        log.error("执行错误");
        throw new RuntimeException("抛出异常");
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Person {
        private String name;
        private int age;
    }
}
