package com.busekylin.springboottest.web;

import com.busekylin.springboottest.domain.Web;
import com.busekylin.springboottest.service.WebService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebController {
    private final WebService webService;

    @GetMapping("/web")
    public Web getWeb() {
        return webService.getWeb();
    }

    @PostMapping("/web")
    public void addWeb(@RequestBody Web web) {
        webService.addWeb(web);
    }
}
