package com.innowise.gateway.dto.response;

import com.innowise.gateway.dto.UserRequestAndResponse;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

public class OrderResponse {
    private Long id;

    private Long userId;

    @NotBlank
    private String status;

    @PositiveOrZero
    private double totalPrice;

    @Email
    @NotBlank
    private String email;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private UserRequestAndResponse user;

    public UserRequestAndResponse getUser() {
        return user;
    }

    public void setUser(UserRequestAndResponse user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
