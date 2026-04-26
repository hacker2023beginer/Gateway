package com.innowise.gateway.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public class PaymentResponse {

    private String id;
    private String orderId;
    private String userId;
    private String status;
    private Instant timestamp;
    private BigDecimal paymentAmount;

    public PaymentResponse() {
    }

    public PaymentResponse(String id, String orderId, String userId, String status, Instant timestamp, BigDecimal paymentAmount) {
        this.id = id;
        this.orderId = orderId;
        this.userId = userId;
        this.status = status;
        this.timestamp = timestamp;
        this.paymentAmount = paymentAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
}
