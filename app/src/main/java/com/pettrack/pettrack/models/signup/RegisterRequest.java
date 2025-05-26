package com.pettrack.pettrack.models.signup;

public class RegisterRequest {
    private String correoElectronico; // Cambiado a camelCase
    private String contrasena;
    private String nombre;
    private String numero;
    private String fechaNacimiento; // Cambiado a camelCase
    private String apellidos;

    public RegisterRequest(String nombre, String apellidos, String fechaNacimiento,
                           String numero, String correoElectronico, String contrasena) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.numero = numero;
        this.correoElectronico = correoElectronico;
        this.contrasena = contrasena;
    }

    // Getters (manteniendo camelCase)
    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNumero() {
        return numero;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getApellidos() {
        return apellidos;
    }
}