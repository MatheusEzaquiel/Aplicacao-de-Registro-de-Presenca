package com.mbe.ada.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class APIService {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Mono<String> makeRequest(String url, String requestBody) {
        // Use webClientBuilder.build() para criar um WebClient
        WebClient webClient = webClientBuilder.baseUrl(url).build();

        // Agora você pode usar o webClient para fazer a requisição
        return webClient.post()
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class);
    }
}
