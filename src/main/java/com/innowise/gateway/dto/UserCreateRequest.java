package com.innowise.gateway.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class UserCreateRequest {
    private String name;
    private String surname;
    @NotNull
    private LocalDate birthDate;
    private String email;

    public UserCreateRequest(
            String email,
            String name,
            String surname,
            LocalDate birthDate) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
    }

    public UserCreateRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "UserCreateRequest{email='" + email + "', name='" + name + "', surname='" + surname + "', birthDate=" + birthDate + "}";
    }
}
