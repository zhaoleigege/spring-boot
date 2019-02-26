package com.busekylin.springbootcluster.service.impl;

import com.busekylin.springbootcluster.domain.Person;
import com.busekylin.springbootcluster.repository.PersonRepository;
import com.busekylin.springbootcluster.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultPersonService implements PersonService {
    private final PersonRepository personRepository;

    @Override
    public Person getPerson() {
        return personRepository.getPerson();
    }

    @Override
    public List<Person> getPersonList(int size) {
        return personRepository.getPersonList(size);
    }
}
