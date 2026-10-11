package com.commune.commune_backend.dto;

import java.time.LocalDateTime;

public class PostResponse {

    private Integer id;
    private String content;
    private String username;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;

    public PostResponse(
            Integer id,
            String content,
            String username,
            String firstName,
            String lastName,
            LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}