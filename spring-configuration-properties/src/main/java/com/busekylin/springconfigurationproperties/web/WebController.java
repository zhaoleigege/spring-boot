package com.busekylin.springconfigurationproperties.web;

import com.busekylin.springconfigurationproperties.config.WebProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebController {
    private final WebProperties properties;

    @GetMapping("properties")
    public WebProperties properties() {
        return properties;
    }
}
