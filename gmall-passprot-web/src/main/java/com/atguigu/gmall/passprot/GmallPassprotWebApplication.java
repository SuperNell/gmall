package com.atguigu.gmall.passprot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.atguigu.gmall")
public class GmallPassprotWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(GmallPassprotWebApplication.class, args);
	}
}
