package com.cldt.front.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * The class front eureka application.
 *
 * @author zhoukj
 */
@EnableEurekaServer
@SpringBootApplication
public class FrontEurekaApplication {

	/**
	 * The entry point of application.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(FrontEurekaApplication.class, args);
	}
}
