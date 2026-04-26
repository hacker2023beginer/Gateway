package com.innowise.gateway.controller;

import com.innowise.gateway.dto.UserRequestAndResponse;
import com.innowise.gateway.dto.request.PaymentRequest;
import com.innowise.gateway.dto.response.PaymentResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/payments")
public class PaymentServiceController {
    @Value("${services.payment-service.url}")
    private String paymentServiceUrl;

    private final WebClient webClient;

    public PaymentServiceController(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    @PostMapping
    public Mono<ResponseEntity<PaymentResponse>> create(@RequestBody @Valid PaymentRequest dto) {
        return webClient.post()
                .uri(paymentServiceUrl + "/payments")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(PaymentResponse.class)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .build())
                );
    }

    @GetMapping
    public Mono<ResponseEntity<List<PaymentResponse>>> getPayments(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String orderId,
            @RequestParam(required = false) String status
    ) {
        StringBuilder uri = new StringBuilder(paymentServiceUrl + "/payments");
        boolean hasQuery = false;

        if (userId != null) {
            uri.append("?userId=").append(userId);
            hasQuery = true;
        }
        if (orderId != null) {
            uri.append(hasQuery ? "&" : "?").append("orderId=").append(orderId);
            hasQuery = true;
        }
        if (status != null) {
            uri.append(hasQuery ? "&" : "?").append("status=").append(status);
        }

        return webClient.get()
                .uri(uri.toString())
                .retrieve()
                .bodyToFlux(PaymentResponse.class)
                .collectList()
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .build())
                );
    }

    @GetMapping("/sum/user")
    public Mono<ResponseEntity<BigDecimal>> getUserSum(
            @RequestParam String userId,
            @RequestParam String from,
            @RequestParam String to
    ) {
        String urlWithParams = String.format("%s/payments/sum/user?userId=%s&from=%s&to=%s",
                paymentServiceUrl, userId, from, to);

        return webClient.get()
                .uri(urlWithParams)
                .retrieve()
                .bodyToMono(BigDecimal.class)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .build())
                );
    }

    @GetMapping("/sum")
    public Mono<ResponseEntity<BigDecimal>> getTotalSum(
            @RequestParam String from,
            @RequestParam String to
    ) {
        String urlWithParams = String.format("%s/payments/sum?from=%s&to=%s",
                paymentServiceUrl, from, to);

        return webClient.get()
                .uri(urlWithParams)
                .retrieve()
                .bodyToMono(BigDecimal.class)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .build())
                );
    }
}
