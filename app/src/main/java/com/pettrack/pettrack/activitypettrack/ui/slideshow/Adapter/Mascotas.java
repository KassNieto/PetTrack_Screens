package com.pettrack.pettrack.activitypettrack.ui.slideshow.Adapter;

public class Mascotas {
    private int id;
    private String imagen;
    private String nombre;
    private String edad;
    private String tipo;

    public Mascotas(int id, String imagen, String nombre, String edad, String tipo) {
        this.id = id;
        this.imagen = imagen;
        this.nombre = nombre;
        this.edad = edad;
        this.tipo = tipo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Mascotas(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Mascotas{" +
                "id=" + id +
                ", imagen='" + imagen + '\'' +
                ", nombre='" + nombre + '\'' +
                ", edad='" + edad + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
