package com.busekylin.springbootexception;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class SpringbootExceptionApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootExceptionApplication.class, args);
	}

	/**
	 * 参考：https://github.com/spring-projects/spring-security/issues/4467
	 * spring boot2中必须开启对error路径的强制验证才能使如下的bean启用
	 * .antMatchers("/error").permitAll()
	 * @return
	 */
	@Bean
	public ErrorAttributes errorAttributes(){
		return new DefaultErrorAttributes(){
			@Override
			public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
				Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);

				Throwable throwable = getError(webRequest);
				Throwable cause = throwable.getCause();

				if (cause != null) {
					Map<String, Object> causeErrorAttributes = new HashMap<>();
					causeErrorAttributes.put("exception", cause.getClass().getName());
					causeErrorAttributes.put("message", cause.getMessage());
					causeErrorAttributes.put("custom", "随便的错误信息");
					errorAttributes.put("cause", causeErrorAttributes);
				}
				return errorAttributes;
			}
		};
	}
}
