package com.pettrack.pettrack.models.cartillavacunacion;
import com.google.gson.annotations.SerializedName;


public class Vacuna {
    private int id;
    private String fechaAplicacion;
    @SerializedName("fechaProximaAplicacion")
    private String proximaAplicacion;

    private String tipoVacuna;
    private int mascotaId;

    // Constructor vacío (requerido por Retrofit/GSON)
    public Vacuna() {}

    // Constructor completo
    public Vacuna(String fechaAplicacion, String proximaAplicacion, String tipoVacuna, int mascotaId) {
        this.fechaAplicacion = fechaAplicacion;
        this.proximaAplicacion = proximaAplicacion;
        this.tipoVacuna = tipoVacuna;
        this.mascotaId = mascotaId;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public String getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(String fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public String getProximaAplicacion() {
        return proximaAplicacion;
    }

    public void setProximaAplicacion(String proximaAplicacion) {
        this.proximaAplicacion = proximaAplicacion;
    }

    public String getTipoVacuna() {
        return tipoVacuna;
    }

    public void setTipoVacuna(String tipoVacuna) {
        this.tipoVacuna = tipoVacuna;
    }

    public int getMascotaId() {
        return mascotaId;
    }

    public void setMascotaId(int mascotaId) {
        this.mascotaId = mascotaId;
    }
}
