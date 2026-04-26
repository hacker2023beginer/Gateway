package com.innowise.gateway.dto.request;

public class AuthRegisterRequest {
    private Long userId;
    private String login;
    private String password;

    public AuthRegisterRequest() {
    }

    public AuthRegisterRequest(Long userId, String login, String password) {
        this.userId = userId;
        this.login = login;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
