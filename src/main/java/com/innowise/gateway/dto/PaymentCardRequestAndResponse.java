package com.innowise.gateway.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public class PaymentCardRequestAndResponse {
    private Long id;

    @NotBlank
    @Pattern(regexp = "\\d{16}")
    private String number;

    @NotBlank
    private String holder;

    @NotNull
    private LocalDate expirationDate;

    private Boolean active;

    @NotNull
    private Long userId;

    public PaymentCardRequestAndResponse(Long id, String number, String holder, LocalDate expirationDate, Boolean active, Long userId) {
        this.id = id;
        this.number = number;
        this.holder = holder;
        this.expirationDate = expirationDate;
        this.active = active;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
