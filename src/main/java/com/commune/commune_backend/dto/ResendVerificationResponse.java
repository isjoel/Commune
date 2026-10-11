
package com.commune.commune_backend.dto;

import java.time.LocalDateTime;

public class ResendVerificationResponse {

    private String message;
    private LocalDateTime expiresAt;

    public ResendVerificationResponse(String message, LocalDateTime expiresAt) {
        this.message = message;
        this.expiresAt = expiresAt;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
}
