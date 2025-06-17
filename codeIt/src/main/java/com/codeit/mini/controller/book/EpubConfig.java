package com.codeit.mini.controller.book;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class EpubConfig implements WebMvcConfigurer {
	
	private final String uploadPath = System.getProperty("user.home") + "/epubs/";
	
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/uploadepub/**")
				.addResourceLocations("file:" + uploadPath);
	}
}
