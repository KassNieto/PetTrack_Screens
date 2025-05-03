package com.pettrack.pettrack.models.signup;

import com.pettrack.pettrack.models.User;

public class ApiResponse {
    private boolean success;
    private String message;
    private User data; // Reutilizando tu clase User existente

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public User getData() {
        return data;
    }

    // Setters
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(User data) {
        this.data = data;
    }
}
