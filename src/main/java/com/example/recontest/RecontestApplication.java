package com.example.recontest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan(basePackages = "com.example")
public class RecontestApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecontestApplication.class, args);
	}

}
