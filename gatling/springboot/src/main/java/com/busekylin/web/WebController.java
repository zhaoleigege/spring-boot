package com.busekylin.web;

import com.busekylin.domain.ClassRoom;
import com.busekylin.domain.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class WebController {
    @GetMapping("/persons")
    public List<Person> getPersons() {
        log.info("请求获得个人信息");
        List<Person> personList = new ArrayList<>();

        for (int i = 0; i < 10_000; i++) {
            personList.add(new Person(
                    "赵磊",
                    (int) (Math.random() * 50)
            ));
        }
        return personList;
    }

    @GetMapping("/classRooms")
    public List<ClassRoom> getClassRooms() {
        log.info("请求获得教室信息");
        List<ClassRoom> classRoomList = new ArrayList<>();

        for (int i = 0; i < 10_000; i++) {
            classRoomList.add(new ClassRoom(
                    "course" + i,
                    "teacher" + i,
                    "description" + i
            ));
        }
        return classRoomList;
    }
}
