package com.innowise.gateway.controller;

import com.innowise.gateway.dto.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserServiceController {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    @Value("${services.user-service.url}")
    private String userServiceUrl;

    @Value("${services.auth-service.url}")
    private String authServiceUrl;
    private final WebClient webClient;

    public UserServiceController(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    @GetMapping("/email")
    public Mono<ResponseEntity<UserResponse>> getUserByEmail(@RequestParam String email) {

        return webClient.get()
                .uri(userServiceUrl + "/users/by-email?email=" + email)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .build())
                );
    }

}
