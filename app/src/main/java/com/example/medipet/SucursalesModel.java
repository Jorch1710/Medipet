package com.example.medipet;

import java.time.LocalTime;

public class SucursalesModel {
    private String nombre;
    private String direccion;
    private String horario;
    private int imagen;
    private LocalTime horaApertura;
    private LocalTime horaCierre;

    public SucursalesModel(String nombre, String direccion, String horario, int imagen, LocalTime horaApertura, LocalTime horaCierre) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.horario = horario;
        this.imagen = imagen;
        this.horaApertura = horaApertura;
        this.horaCierre = horaCierre;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getHorario() {
        return horario;
    }

    public int getImagen() {
        return imagen;
    }

    public LocalTime getHoraApertura() {
        return horaApertura;
    }

    public LocalTime getHoraCierre() {
        return horaCierre;
    }

    // Setters (opcional, si necesitas modificar los valores)
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public void setHoraApertura(LocalTime horaApertura) {
        this.horaApertura = horaApertura;
    }

    public void setHoraCierre(LocalTime horaCierre) {
        this.horaCierre = horaCierre;
    }
}
