package com.busekylin.springbootcluster.service;


import com.busekylin.springbootcluster.domain.Person;

import java.util.List;

public interface PersonService {
    Person getPerson();

    List<Person> getPersonList(int size);
}
