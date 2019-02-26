package com.busekylin.springbootcluster.repository;


import com.busekylin.springbootcluster.domain.Person;

import java.util.List;

public interface PersonRepository {
    Person getPerson();

    List<Person> getPersonList(int size);
}
