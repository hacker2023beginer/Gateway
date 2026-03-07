package com.innowise.gateway.controller;

import com.innowise.gateway.dto.PaymentCardRequestAndResponse;
import com.innowise.gateway.dto.UserRequestAndResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import java.util.List;


@RestController
@RequestMapping("/users")
public class UserServiceController {
    public static final String USER_PATH = "/users/";
    @Value("${services.user-service.url}")
    private String userServiceUrl;
    private final WebClient webClient;

    public UserServiceController(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    @GetMapping("/email")
    public Mono<ResponseEntity<UserRequestAndResponse>> getUserByEmail(@RequestParam String email, ServerHttpRequest request) {

        return webClient.get()
                .uri(userServiceUrl + "/users/by-email?email=" + email)
                .header(HttpHeaders.AUTHORIZATION,
                        request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .retrieve()
                .bodyToMono(UserRequestAndResponse.class)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .build())
                );
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<UserRequestAndResponse>> createUser(
            @RequestBody @Valid UserRequestAndResponse requestBody,
            ServerHttpRequest request
    ) {
        return webClient.post()
                .uri(userServiceUrl + "/users")
                .header(HttpHeaders.AUTHORIZATION,
                        request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(UserRequestAndResponse.class)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity.status(ex.getStatusCode()).build())
                );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserRequestAndResponse>> getUserById(@PathVariable Long id, ServerHttpRequest request) {
        return webClient.get()
                .uri(userServiceUrl + USER_PATH + id)
                .header(HttpHeaders.AUTHORIZATION,
                        request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .retrieve()
                .bodyToMono(UserRequestAndResponse.class)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity.status(ex.getStatusCode()).build())
                );
    }

    @GetMapping("/{id}/cards")
    public Mono<ResponseEntity<List<PaymentCardRequestAndResponse>>> getUserCardsById(
            @PathVariable Long id,
            ServerHttpRequest request) {

        return webClient.get()
                .uri(userServiceUrl + USER_PATH + id + "/cards")
                .header(HttpHeaders.AUTHORIZATION,
                        request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .retrieve()
                .bodyToFlux(PaymentCardRequestAndResponse.class)
                .collectList()
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .build())
                );
    }

    //issue with Page
    @GetMapping
    public Mono<ResponseEntity<Page<UserRequestAndResponse>>> getUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            ServerHttpRequest request
    ) {

        StringBuilder uri = new StringBuilder(userServiceUrl + "/users?page=" + page + "&size=" + size);

        if (name != null) {
            uri.append("&name=").append(name);
        }

        if (surname != null) {
            uri.append("&surname=").append(surname);
        }

        return webClient.get()
                .uri(uri.toString())
                .header(HttpHeaders.AUTHORIZATION,
                        request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageImpl<UserRequestAndResponse>>() {
                })
                .map(ResponseEntity::ok);
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<UserRequestAndResponse>> getUserById(
            @PathVariable Long id,
            @RequestBody @Valid UserRequestAndResponse userRequest,
            ServerHttpRequest request) {
        return webClient.put()
                .uri(userServiceUrl + USER_PATH + id)
                .header(HttpHeaders.AUTHORIZATION,
                        request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .bodyValue(userRequest)
                .retrieve()
                .bodyToMono(UserRequestAndResponse.class)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .build())
                );
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Void>> deleteUserById(@PathVariable Long id, ServerHttpRequest request) {
        return webClient.delete()
                .uri(userServiceUrl + "/users?id=" + id)
                .header(HttpHeaders.AUTHORIZATION,
                        request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .retrieve()
                .bodyToMono(Void.class)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .build())
                );
    }

    @PatchMapping("/deactivate/{id}")
    public Mono<ResponseEntity<Void>> deactivateUser(@PathVariable Long id, ServerHttpRequest request) {
        return webClient.patch()
                .uri(userServiceUrl + USER_PATH + id + "/deactivate")
                .header(HttpHeaders.AUTHORIZATION,
                        request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .retrieve()
                .bodyToMono(Void.class)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .build())
                );
    }

    @PatchMapping("/activate/{id}")
    public Mono<ResponseEntity<Void>> activateUser(@PathVariable Long id, ServerHttpRequest request) {
        return webClient.patch()
                .uri(userServiceUrl + USER_PATH + id + "/activate")
                .header(HttpHeaders.AUTHORIZATION,
                        request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .retrieve()
                .bodyToMono(Void.class)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .build())
                );
    }
}

