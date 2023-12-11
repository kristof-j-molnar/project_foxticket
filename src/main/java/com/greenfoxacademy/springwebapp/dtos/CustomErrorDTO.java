package com.greenfoxacademy.springwebapp.dtos;

public class CustomErrorDTO {
    private String error;

    public CustomErrorDTO(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
