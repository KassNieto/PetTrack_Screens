package com.pettrack.pettrack.models.signup;

import com.pettrack.pettrack.models.User;

public class ApiResponse {
    private boolean success;
    private String message;
    private int id;  // Nuevo campo para el ID
    private String nombre;
    private String apellidos;
    private String fechaNacimiento;
    private String numero;
    private String correoElectronico;
    private String contrasena;

    // Getters y setters
    public boolean isSuccess() {
        return success;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    // ... otros getters
}
