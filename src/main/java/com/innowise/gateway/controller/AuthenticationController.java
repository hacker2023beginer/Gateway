package com.innowise.gateway.controller;

import com.innowise.gateway.dto.*;
import com.innowise.gateway.dto.request.CredentialsRequest;
import com.innowise.gateway.dto.request.LoginRequest;
import com.innowise.gateway.dto.request.RegistrationRequest;
import com.innowise.gateway.dto.request.UserCreateRequest;
import com.innowise.gateway.dto.response.AuthResponse;
import com.innowise.gateway.dto.response.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@RestController
public class AuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    @Value("${services.user-service.url}")
    private String userServiceUrl;
    @Value("${services.auth-service.url}")
    private String authServiceUrl;

    private final WebClient webClient;

    public AuthenticationController(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<String>> register(@RequestBody RegistrationRequest request) {
        UserCreateRequest userDto = request.toUserDto();
        return webClient.post()
                .uri(userServiceUrl + "/users")
                .bodyValue(userDto)
                .retrieve()
                .bodyToMono(UserRequestAndResponse.class)
                .doOnError(WebClientResponseException.class, ex ->
                        log.error("Error from user-service: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString()))
                .flatMap(userRequestAndResponse -> {
                    Long userId = userRequestAndResponse.getId();

                    return webClient.post()
                            .uri(authServiceUrl + "/auth/register")
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
                .uri(userServiceUrl + "/users/rollback/" + userId)
                .retrieve()
                .bodyToMono(Void.class)
                .thenReturn(
                        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Registration failed. Rolled back.")
                );
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponse>> login(@RequestBody LoginRequest request){
        return webClient.post()
                .uri(authServiceUrl + "/auth/login")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(LoginResponse.class)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .build())
                );
    }

    @PostMapping("/credentials")
    public Mono<ResponseEntity<String>> saveCredentials(@RequestBody CredentialsRequest request){
        return webClient.post()
                .uri(authServiceUrl + "/auth/credentials")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CredentialsRequest.class)
                .map(authResponse -> ResponseEntity.ok("Credentials saved"))
                .onErrorResume(error -> {
                    if (error instanceof WebClientResponseException ex) {
                        log.error("Auth credentials error: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());
                        return Mono.just(
                                ResponseEntity.status(ex.getStatusCode())
                                        .body("Login failed: " + ex.getResponseBodyAsString())
                        );
                    }

                    log.error("Unexpected credentials error: {}", error.getMessage());
                    return Mono.just(
                            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("Auth credentials failed due to internal error")
                    );
                });
    }
}
