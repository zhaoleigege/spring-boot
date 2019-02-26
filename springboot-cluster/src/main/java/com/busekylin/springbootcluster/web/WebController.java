package com.busekylin.springbootcluster.web;

import com.busekylin.springbootcluster.domain.Person;
import com.busekylin.springbootcluster.domain.UserInfo;
import com.busekylin.springbootcluster.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequiredArgsConstructor

public class WebController {
    @Value("${serverID}")
    private String serverID;

    @GetMapping("session")
    public UserInfo getSession(HttpSession session) {
        return (UserInfo) session.getAttribute("name");
    }

    @GetMapping("info")
    public UserInfo setInfo(HttpSession session) {
        UserInfo userInfo = new UserInfo();
        userInfo.setServerID(serverID);
        userInfo.setSessionID(session.getId());
        session.setAttribute("name", userInfo);
        return userInfo;
    }

    private final PersonService personService;

    @GetMapping("person")
    public Person getPerson() {
        return personService.getPerson();
    }


    @GetMapping("persons/{size}")
    public List<Person> getPersons(@PathVariable Integer size) {
        return personService.getPersonList(size);
    }
}
