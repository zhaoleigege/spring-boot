package com.busekylin.springbootcluster.repository.impl;

import com.busekylin.springbootcluster.domain.Person;
import com.busekylin.springbootcluster.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
