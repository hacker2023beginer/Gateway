package com.innowise.gateway.dto.request;

import com.innowise.gateway.dto.Role;

public class CredentialsRequest {
    private Long userId;
    private String login;
    private String password;
    private Role role;

    public CredentialsRequest() {
    }

    public CredentialsRequest(Long userId, String login, String password, Role role) {
        this.userId = userId;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
