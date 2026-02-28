package com.innowise.gateway.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.gateway.dto.AuthResponse;
import com.innowise.gateway.dto.RegistrationRequest;
import com.innowise.gateway.dto.UserCreateRequest;
import com.innowise.gateway.dto.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public RegistrationController(WebClient.Builder builder, ObjectMapper objectMapper) {
        this.webClient = builder.build();
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public Mono<ResponseEntity<String>> register(@RequestBody RegistrationRequest request) {
        UserCreateRequest userDto = request.toUserDto();
        try {
            log.info("Sending to user-service: {} -> JSON: {}", userDto, objectMapper.writeValueAsString(userDto));
        } catch (JsonProcessingException e) {
            log.warn("Could not serialize userDto for logging: {}", e.getMessage());
        }

        return webClient.post()
                .uri("http://localhost:8082/users")
                .bodyValue(userDto)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .doOnError(WebClientResponseException.class, ex ->
                        log.error("Error from user-service: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString()))
                .flatMap(userResponse -> {
                    Long userId = userResponse.getId();

                    return webClient.post()
                            .uri("http://localhost:8081/auth/register")
                            .bodyValue(request.toAuthDto(userId))
                            .retrieve()
                            .bodyToMono(AuthResponse.class)
                            .map(authResp -> ResponseEntity.ok("User created"))
                            .onErrorResume(error -> {
                                if (error instanceof WebClientResponseException ex) {
                                    log.error("Error from auth: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());
                                }
                                return rollbackUser(userId);
                            });
                });
    }

    private Mono<ResponseEntity<String>> rollbackUser(Long userId) {
        return webClient.delete()
                .uri("http://localhost:8082/users/rollback/" + userId)
                .retrieve()
                .bodyToMono(Void.class)
                .thenReturn(
                        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Registration failed. Rolled back.")
                );
    }
}
