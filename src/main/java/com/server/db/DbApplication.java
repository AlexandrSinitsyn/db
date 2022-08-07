package com.server.db;

import com.server.db.annotations.AccessInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class DbApplication implements WebMvcConfigurer {
	private AccessInterceptor accessInterceptor;

	@Autowired
	public void setAccessInterceptor(AccessInterceptor accessInterceptor) {
		this.accessInterceptor = accessInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(accessInterceptor);
	}

	public static void main(String[] args) {
		SpringApplication.run(DbApplication.class, args);
	}

}
