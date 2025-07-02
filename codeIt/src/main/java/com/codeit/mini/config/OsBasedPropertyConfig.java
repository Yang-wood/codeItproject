package com.codeit.mini.config;

import java.io.IOException;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
public class OsBasedPropertyConfig {
	
	
	@Bean
	@Primary
	public PropertySourcesPlaceholderConfigurer osPropertyConfigurer() throws IOException {
		PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
		Properties properties = new Properties();
		
		String os = System.getProperty("os.name").toLowerCase();
		
		System.out.println(">>>>>>>>>>>>>>> OS 기반 설정 로드 시도");
		
		if (os.contains("win")) {
			log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>> 윈도우용 OS 환경 설정");
            properties.load(new ClassPathResource("application-windows.properties").getInputStream());
        } else if (os.contains("mac")) {
        	log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>> 맥용 OS 환경 설정");
            properties.load(new ClassPathResource("application-mac.properties").getInputStream());
        } else {
        	log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>> 리눅스용 OS 환경 설정");
            properties.load(new ClassPathResource("application-linux.properties").getInputStream());
        }
		
		configurer.setProperties(properties);
        return configurer;
	}
}
