package com.busekylin.springconfigurationproperties.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties("web")
@Data
public class WebProperties {
    private String name;
    private String[] urls;
    private Map<String, String> map;

    private InnerClass inner;

    @Data
    static class InnerClass {
        int deep;
    }
}
