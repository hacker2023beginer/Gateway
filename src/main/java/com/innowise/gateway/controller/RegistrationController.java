package com.innowise.gateway.controller;

import com.innowise.gateway.dto.AuthResponse;
import com.innowise.gateway.dto.RegistrationRequest;
import com.innowise.gateway.dto.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    private final WebClient webClient;

    public RegistrationController(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    @PostMapping
    public Mono<ResponseEntity<String>> register(@RequestBody RegistrationRequest request) {

        return webClient.post()
                .uri("http://localhost:8082/users")
                .bodyValue(request.toUserDto())
                .retrieve()
                .bodyToMono(UserResponse.class)

                .flatMap(userResponse -> {
                    Long userId = userResponse.getId();

                    return webClient.post()
                            .uri("http://localhost:8081/auth/register")
                            .bodyValue(request.toAuthDto(userId))
                            .retrieve()
                            .bodyToMono(AuthResponse.class)
                            .map(authResp -> ResponseEntity.ok("User created"))
                            .onErrorResume(error -> rollbackUser(userId));
                });
    }

    // rollback user-service
    private Mono<ResponseEntity<String>> rollbackUser(Long userId) {
        return webClient.delete()
                .uri("http://localhost:8082/users/" + userId)
                .retrieve()
                .bodyToMono(Void.class)
                .thenReturn(
                        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Registration failed. Rolled back.")
                );
    }
}
