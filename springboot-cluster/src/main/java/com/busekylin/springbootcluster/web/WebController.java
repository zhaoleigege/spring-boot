package com.busekylin.springbootcluster.web;


import com.busekylin.springbootcluster.domain.UserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class WebController {
    @Value("${serverID}")
    private String serverID;

    @GetMapping("session")
    public UserInfo getSession(HttpSession session) {
        System.out.println(session.getId());
        return (UserInfo) session.getAttribute("name");
    }

    @GetMapping("info")
    public UserInfo setInfo(HttpSession session) {
        UserInfo userInfo = new UserInfo();
        userInfo.setServerID(serverID);
        userInfo.setSessionID(session.getId());
        session.setAttribute("name", userInfo);
        System.out.println(session.getId());
        return userInfo;
    }
}
