package com.innowise.gateway.controller;

import com.innowise.gateway.dto.request.ItemRequest;
import com.innowise.gateway.dto.request.OrderItemRequest;
import com.innowise.gateway.dto.request.OrderRequest;
import com.innowise.gateway.dto.response.ItemResponse;
import com.innowise.gateway.dto.response.OrderItemResponse;
import com.innowise.gateway.dto.response.OrderResponse;
import com.innowise.gateway.dto.response.PageResponse;
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
@RequestMapping("/orders")
public class OrderServiceController {
    public static final String ORDER_ITEM_PATH = "/orderitems/";
    public static final String ORDER_PATH = "/orders/";
    public static final String ITEM_PATH = "/items/";
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
                .uri(orderServiceUrl + ITEM_PATH + id)
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
                .uri(orderServiceUrl + ITEM_PATH + id)
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
                .uri(orderServiceUrl + ITEM_PATH + id)
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
    public Mono<ResponseEntity<OrderResponse>> getOrderById(@PathVariable Long id, ServerHttpRequest request){
        return webClient.get()
                .uri(orderServiceUrl + ORDER_PATH + id)
                .header(HttpHeaders.AUTHORIZATION, request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
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
    public Mono<ResponseEntity<PageResponse<OrderResponse>>> getOrders(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            ServerHttpRequest request
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
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        return webClient.get()
                .uri(uri.toString())
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageResponse<OrderResponse>>() {})
                .map(ResponseEntity::ok);
    }

    @GetMapping("/byuserid/{id}")
    public Mono<ResponseEntity<List<OrderResponse>>> getOrdersByUserId(@PathVariable Long id, ServerHttpRequest request){
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        return webClient.get()
                .uri(orderServiceUrl + "/orders/userid/" + id)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
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
    public Mono<ResponseEntity<OrderResponse>> updateOrderById(@PathVariable Long id, @RequestBody OrderRequest orderRequest,
                                                               ServerHttpRequest request){
        return webClient.put()
                .uri(orderServiceUrl + ORDER_PATH + id)
                .header(HttpHeaders.AUTHORIZATION, request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
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
                .uri(orderServiceUrl + ORDER_PATH + id)
                .retrieve()
                .bodyToMono(Void.class)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .build())
                );
    }

    @PostMapping("/orderitems")
    public Mono<ResponseEntity<OrderItemResponse>> createOrderItem(@RequestBody @Valid OrderItemRequest orderItemRequest){
        return webClient.post()
                .uri(orderServiceUrl + "/orderitems")
                .bodyValue(orderItemRequest)
                .retrieve()
                .bodyToMono(OrderItemResponse.class)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .build())
                );
    }

    @GetMapping("/orderitems/{id}")
    public Mono<ResponseEntity<OrderItemResponse>> getOrderItemById(@PathVariable Long id){
        return webClient.get()
                .uri(orderServiceUrl + ORDER_ITEM_PATH + id)
                .retrieve()
                .bodyToMono(OrderItemResponse.class)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .build())
                );
    }

    @PutMapping("/orderitems/{id}")
    public Mono<ResponseEntity<OrderItemResponse>> updateOrderItemById(@PathVariable Long id, @RequestBody OrderItemRequest orderItemRequest){
        return webClient.put()
                .uri(orderServiceUrl + ORDER_ITEM_PATH + id)
                .bodyValue(orderItemRequest)
                .retrieve()
                .bodyToMono(OrderItemResponse.class)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .build())
                );
    }

    @DeleteMapping("/orderitems/{id}")
    public Mono<ResponseEntity<Void>> deleteOrderItemById(@PathVariable Long id){
        return webClient.delete()
                .uri(orderServiceUrl + ORDER_ITEM_PATH + id)
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
