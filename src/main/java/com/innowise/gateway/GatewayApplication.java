package com.innowise.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.autoconfigure.LifecycleMvcEndpointAutoConfiguration;

@SpringBootApplication(exclude = {
		LifecycleMvcEndpointAutoConfiguration.class
})
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

}
