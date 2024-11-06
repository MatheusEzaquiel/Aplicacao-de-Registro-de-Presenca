package com.mbe.ada.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class APIConfig {

	@Bean
	public WebClient.Builder webClientBuilder() {
		return WebClient.builder();
	}

}
