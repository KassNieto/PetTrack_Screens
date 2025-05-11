
// User.java (si es necesario)
package com.pettrack.pettrack.models;

public class User {
    private Integer id;
    private String nombre;
    private String apellidos;
    private String fechaNacimiento;
    private String numero;
    private String correoElectronico;
    private String contrasena;

    // Getters y Setters
    public Integer getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public String getFechaNacimiento() { return fechaNacimiento; }
    public String getNumero() { return numero; }
    public String getCorreoElectronico() { return correoElectronico; }
    // ... setters si los necesitas
}