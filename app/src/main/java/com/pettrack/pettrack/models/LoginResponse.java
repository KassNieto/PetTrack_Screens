// LoginResponse.java
package com.pettrack.pettrack.models;

public class LoginResponse {
    private String token;
    private String user_id;  // Nuevo campo
    private User user;      // Objeto completo (opcional)

    // Getters y setters
    public String getToken() {
        return token;
    }

    public String getUserId() {  // Nuevo getter
        return user_id;
    }

    public User getUser() {
        return user;
    }
}