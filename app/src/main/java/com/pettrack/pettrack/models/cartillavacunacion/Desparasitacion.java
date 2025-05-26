
package com.pettrack.pettrack.models.cartillavacunacion;

import com.google.gson.annotations.SerializedName;
import com.pettrack.pettrack.models.Mascota;

public class Desparasitacion {
    private int id;
    private String fechaAplicacion;

    @SerializedName("fechaProximaAplicacion")
    private String fechaProximaAplicacion;

    private String dosis;
    private double pesoKg;
    private Mascota mascota;

    // Constructor vacío requerido por Retrofit
    public Desparasitacion() {}

    // Constructor con parámetros
    public Desparasitacion(String fechaAplicacion, String fechaProximaAplicacion,
                           String dosis, double pesoKg, int mascotaId) {
        this.fechaAplicacion = fechaAplicacion;
        this.fechaProximaAplicacion = fechaProximaAplicacion;
        this.dosis = dosis;
        this.pesoKg = pesoKg;

        // Creamos el objeto Mascota con sólo el ID
        Mascota m = new Mascota();
        m.setId(mascotaId);
        this.mascota = m;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(String fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public String getFechaProximaAplicacion() {
        return fechaProximaAplicacion;
    }

    public void setFechaProximaAplicacion(String fechaProximaAplicacion) {
        this.fechaProximaAplicacion = fechaProximaAplicacion;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public double getPesoKg() {
        return pesoKg;
    }

    public void setPesoKg(double pesoKg) {
        this.pesoKg = pesoKg;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }
}
