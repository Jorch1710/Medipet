package com.example.medipet;

import java.time.LocalTime;

public class SucursalesModel {

    String sucursalNombre;
    String sucursalDireccion;
    String sucursalHorario;
    int sucursalImagen;

    LocalTime SucursalInicioAtencion;
    LocalTime SucursalFinAtencion;

    public SucursalesModel(String sucursalNombre, String sucursalDireccion, String sucursalHorario, int sucursalImagen, LocalTime sucursalInicioAtencion, LocalTime sucursalFinAtencion) {
        this.sucursalNombre = sucursalNombre;
        this.sucursalDireccion = sucursalDireccion;
        this.sucursalHorario = sucursalHorario;
        this.sucursalImagen = sucursalImagen;
        this.SucursalInicioAtencion=sucursalInicioAtencion;
        this.SucursalFinAtencion=sucursalFinAtencion;
    }

    public String getSucursalNombre() {
        return sucursalNombre;
    }

    public String getSucursalDireccion() {
        return sucursalDireccion;
    }

    public String getSucursalHorario() {
        return sucursalHorario;
    }

    public int getSucursalImagen() {
        return sucursalImagen;
    }

    public LocalTime getSucursalInicioAtencion() {
        return SucursalInicioAtencion;
    }

    public LocalTime getSucursalFinAtencion() {
        return SucursalFinAtencion;
    }

}
