package com.cldt.front.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * The class Paas cloud discovery application.
 *
 * @author zhoukj
 */
@SpringBootApplication
@EnableConfigServer
@EnableEurekaClient
public class FrontDiscoveryApplication {

	/**
	 * The entry point of application.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(FrontDiscoveryApplication.class, args);
	}
}
