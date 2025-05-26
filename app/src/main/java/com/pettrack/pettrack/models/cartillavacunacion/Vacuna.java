package com.pettrack.pettrack.models.cartillavacunacion;

import com.google.gson.annotations.SerializedName;
import com.pettrack.pettrack.models.Mascota;

public class Vacuna {
    private int id;
    private String fechaAplicacion;

    @SerializedName("fechaProximaAplicacion")
    private String proximaAplicacion;

    private String tipoVacuna;
    private Mascota mascota;

    // Constructor vacío (requerido por Retrofit/GSON)
    public Vacuna() {}

    // Constructor completo que recibe idMascota e inicializa mascota
    public Vacuna(String fechaAplicacion, String proximaAplicacion, String tipoVacuna, int mascotaId) {
        this.fechaAplicacion = fechaAplicacion;
        this.proximaAplicacion = proximaAplicacion;
        this.tipoVacuna = tipoVacuna;

        // Creamos el objeto Mascota con sólo el ID
        Mascota m = new Mascota();
        m.setId(mascotaId);
        this.mascota = m;
    }

    // Getters y setters
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

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }
}
