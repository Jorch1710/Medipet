package com.example.medipet; // Asegúrate que el package sea el correcto

public class CitaModel {
    private int id; // ID de la cita de la base de datos
    private int usuarioId;
    private String nombreCitaSucursal;
    private String motivoCita;
    private String direccionSucursal; // Cambié el nombre para que coincida con tu XML
    private String fechaCita;
    private String horaCita;
    private byte[] imagenCitaSucursal; // Cambié el nombre para que coincida con tu XML

    // Constructor vacío (útil para algunas librerías o frameworks)
    public CitaModel() {
    }

    // Constructor con todos los campos (opcional, pero puede ser útil)
    public CitaModel(int id, int usuarioId, String nombreCitaSucursal, String motivoCita, String direccionSucursal, String fechaCita, String horaCita, byte[] imagenCitaSucursal) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nombreCitaSucursal = nombreCitaSucursal;
        this.motivoCita = motivoCita;
        this.direccionSucursal = direccionSucursal;
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
        this.imagenCitaSucursal = imagenCitaSucursal;
    }

    // Getters y Setters para todos los campos

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombreCitaSucursal() {
        return nombreCitaSucursal;
    }

    public void setNombreCitaSucursal(String nombreCitaSucursal) {
        this.nombreCitaSucursal = nombreCitaSucursal;
    }

    public String getMotivoCita() {
        return motivoCita;
    }

    public void setMotivoCita(String motivoCita) {
        this.motivoCita = motivoCita;
    }

    public String getDireccionSucursal() {
        return direccionSucursal;
    }

    public void setDireccionSucursal(String direccionSucursal) {
        this.direccionSucursal = direccionSucursal;
    }

    public String getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(String fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getHoraCita() {
        return horaCita;
    }

    public void setHoraCita(String horaCita) {
        this.horaCita = horaCita;
    }

    public byte[] getImagenCitaSucursal() {
        return imagenCitaSucursal;
    }

    public void setImagenCitaSucursal(byte[] imagenCitaSucursal) {
        this.imagenCitaSucursal = imagenCitaSucursal;
    }
}