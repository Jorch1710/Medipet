package com.example.medipet;

public class SucursalesModel {

    String sucursalNombre;
    String sucursalDireccion;
    String sucursalHorario;
    int sucursalImagen;

    public SucursalesModel(String sucursalNombre, String sucursalDireccion, String sucursalHorario, int sucursalImagen) {
        this.sucursalNombre = sucursalNombre;
        this.sucursalDireccion = sucursalDireccion;
        this.sucursalHorario = sucursalHorario;
        this.sucursalImagen = sucursalImagen;
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
}
