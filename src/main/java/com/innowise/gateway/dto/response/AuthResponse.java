package com.innowise.gateway.dto.response;


public class AuthResponse {
    private Long userId;
    private String login;

    public AuthResponse() {
    }

    public AuthResponse(Long userId, String login) {
        this.userId = userId;
        this.login = login;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

}
