package com.pettrack.pettrack.models.historialmedico;
import com.google.gson.annotations.SerializedName;

public class HistorialMedicoUploader {
    private int id;
    private String nombre;

    @SerializedName("ruta")
    private String Ruta;

    public HistorialMedicoUploader(int id, String nombre, String Ruta) {
        this.id = id;
        this.nombre = nombre;
        this.Ruta = Ruta;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getRuta() { return Ruta; }

    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setRuta(String Ruta) { this.Ruta = Ruta; }
}