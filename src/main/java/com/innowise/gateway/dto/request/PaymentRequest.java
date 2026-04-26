package com.innowise.gateway.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class PaymentRequest {

    @NotBlank
    private String orderId;

    @NotBlank
    private String userId;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal paymentAmount;

    public PaymentRequest() {
    }

    public PaymentRequest(String orderId, String userId, BigDecimal paymentAmount) {
        this.orderId = orderId;
        this.userId = userId;
        this.paymentAmount = paymentAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
}
