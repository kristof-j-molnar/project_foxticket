package com.greenfoxacademy.springwebapp.dtos;

public class TokenDTO {
    private String status;
    private String token;

    public TokenDTO() {
    }

    public TokenDTO(String status, String token) {
        this.status = status;
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
