package com.innowise.gateway.controller;

import com.innowise.gateway.dto.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderServiceController {
    @Value("${services.order-service.url}")
    private String orderServiceUrl;
    private final WebClient webClient;

    public OrderServiceController(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    @PostMapping("/items/create")
    public Mono<ResponseEntity<ItemResponse>> createItem(@RequestBody ItemRequest itemRequest) {
        return webClient.post()
                .uri(orderServiceUrl + "/items")
                .bodyValue(itemRequest)
                .retrieve()
                .bodyToMono(ItemResponse.class)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .build())
                );
    }

    @GetMapping("/items/{id}")
    public Mono<ResponseEntity<ItemResponse>> getItemById(@PathVariable Long id) {
        return webClient.get()
                .uri(orderServiceUrl + "/items/" + id)
                .retrieve()
                .bodyToMono(ItemResponse.class)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .build())
                );
    }

    @PutMapping("/items/{id}")
    public Mono<ResponseEntity<ItemResponse>> updateItemById(@PathVariable Long id, @RequestBody ItemRequest itemDto) {
        return webClient.put()
                .uri(orderServiceUrl + "/items/" + id)
                .bodyValue(itemDto)
                .retrieve()
                .bodyToMono(ItemResponse.class)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .build())
                );
    }

    @DeleteMapping("/items/{id}")
    public Mono<ResponseEntity<Void>> deleteItemById(@PathVariable Long id) {
        return webClient.delete()
                .uri(orderServiceUrl + "/items/" + id)
                .retrieve()
                .bodyToMono(Void.class)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .build())
                );
    }

    @PostMapping("/createorder")
    public Mono<ResponseEntity<OrderResponse>> createOrder(@RequestBody @Valid OrderRequest orderRequest, ServerHttpRequest request){
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        System.out.println("GATEWAY HEADER: " + authHeader);
        return webClient.post()
                .uri(orderServiceUrl + "/orders/create")
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .bodyValue(orderRequest)
                .retrieve()
                .bodyToMono(OrderResponse.class)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .build())
                );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<OrderResponse>> getOrderById(@PathVariable Long id){
        return webClient.get()
                .uri(orderServiceUrl + "/orders/" + id)
                .retrieve()
                .bodyToMono(OrderResponse.class)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .build())
                );
    }

    @GetMapping()
    public Mono<ResponseEntity<Page<OrderResponse>>> getOrders(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        StringBuilder uri = new StringBuilder(orderServiceUrl + "/orders?page=" + page + "&size=" + size);

        if (from != null) {
            uri.append("&from=").append(from);
        }

        if (to != null) {
            uri.append("&to=").append(to);
        }

        if (status != null) {
            uri.append("&status=").append(status);
        }

        return webClient.get()
                .uri(uri.toString())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageImpl<OrderResponse>>() {})
                .map(ResponseEntity::ok);
    }

    @GetMapping("/orders/{id}")
    public Mono<ResponseEntity<List<OrderResponse>>> getOrdersByUserId(@PathVariable Long id){
        return webClient.get()
                .uri(orderServiceUrl + "/orders/userid/" + id)
                .retrieve()
                .bodyToFlux(OrderResponse.class)
                .collectList()
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .build())
                );
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<OrderResponse>> updateOrderById(@PathVariable Long id, @RequestBody OrderRequest orderRequest){
        return webClient.put()
                .uri(orderServiceUrl + "/orders/" + id)
                .bodyValue(orderRequest)
                .retrieve()
                .bodyToMono(OrderResponse.class)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .build())
                );
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteOrderById(@PathVariable Long id){
        return webClient.delete()
                .uri(orderServiceUrl + "/orders/" + id)
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
