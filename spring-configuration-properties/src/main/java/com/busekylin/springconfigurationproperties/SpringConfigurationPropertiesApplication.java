package com.busekylin.springconfigurationproperties;

import com.busekylin.springconfigurationproperties.config.WebProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(WebProperties.class)
public class SpringConfigurationPropertiesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringConfigurationPropertiesApplication.class, args);
    }

}

