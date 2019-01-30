package com.busekylin.springbootdocker.web;

import com.busekylin.springbootdocker.domain.Person;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @Value("${name}")
    private String name;

    @Value("${age}")
    private int age;

    @GetMapping("/person")
    public Person getPerson() {
        Person person = new Person();
        person.setAge(age);
        person.setName(name);

        return person;
    }
}
