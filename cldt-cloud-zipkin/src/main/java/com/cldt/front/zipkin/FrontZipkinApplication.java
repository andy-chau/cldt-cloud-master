package com.cldt.front.zipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.sleuth.zipkin.stream.EnableZipkinStreamServer;

/**
 * The class cldt zipkin application.
 *
 * @author zhoukj
 */
@EnableEurekaClient
@SpringBootApplication
@EnableZipkinStreamServer
public class FrontZipkinApplication {

	/**
	 * The entry point of application.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(FrontZipkinApplication.class, args);
	}
}
