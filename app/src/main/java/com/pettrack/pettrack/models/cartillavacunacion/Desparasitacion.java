package com.pettrack.pettrack.models.cartillavacunacion;

public class Desparasitacion {
    private int id;
    private String fechaAplicacion;
    private String proximaAplicacion;
    private String peso;
    private String dosis;
    private int idMascota; // NUEVO campo

    // Constructor vacío requerido por Retrofit
    public Desparasitacion() {}

    // Constructor con parámetros
    public Desparasitacion(String fechaAplicacion, String proximaAplicacion, String dosis, String peso, int idMascota) {
        this.fechaAplicacion = fechaAplicacion;
        this.proximaAplicacion = proximaAplicacion;
        this.dosis = dosis;
        this.peso = peso;
        this.idMascota = idMascota;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public String getFechaAplicacion() {
        return fechaAplicacion;
    }

    public String getProximaAplicacion() {
        return proximaAplicacion;
    }

    public String getPeso() {
        return peso;
    }

    public String getDosis() {
        return dosis;
    }

    public int getIdMascota() {
        return idMascota;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFechaAplicacion(String fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public void setProximaAplicacion(String proximaAplicacion) {
        this.proximaAplicacion = proximaAplicacion;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public void setIdMascota(int idMascota) {
        this.idMascota = idMascota;
    }
}
