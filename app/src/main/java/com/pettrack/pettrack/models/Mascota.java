package com.pettrack.pettrack.models;

public class Mascota {
    private int id;
    private String nombre;
    private String sexo;
    private String raza;
    private String edad;
    private String color;
    private String peso;
    private String seniasParticulares;
    private String foto;

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }

    public String getEdad() { return edad; }
    public void setEdad(String edad) { this.edad = edad; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

    // ... otros getters y setters según necesites
}