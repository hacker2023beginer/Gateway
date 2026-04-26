package com.innowise.gateway.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class UserRequestAndResponse {
    private Long id;
    private String name;
    private String surname;
    @NotNull
    private LocalDate birthDate;
    private String email;
    private boolean active;

    public UserRequestAndResponse() {
    }

    public UserRequestAndResponse(Long id,
                                  String name,
                                  String surname,
                                  LocalDate birthDate,
                                  String email,
                                  boolean active) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.email = email;
        this.active = active;
    }

    public Long getId() {
        return id;
    }


    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
