// LoginResponse.java
package com.pettrack.pettrack.models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    private String token;
    @SerializedName("id")
    private int userId;

    private User user;      // Objeto completo (opcional)

    // Getters y setters
    public int getUserId() {
        return userId;
    }
    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}