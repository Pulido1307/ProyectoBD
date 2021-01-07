package com.pulido.proyectobd.Modelos;

public class Deudor {
    private int IdDeudor;
    private String Nombre;
    private String Apellidos;
    private double SaldoDeudor;
    private String FechaUltimaCompra;
    private String Observaciones;

    public Deudor(){};

    public Deudor(int idDeudor, String nombre, String apellidos, double saldoDeudor, String fechaUltimaCompra, String observaciones) {
        IdDeudor = idDeudor;
        Nombre = nombre;
        Apellidos = apellidos;
        SaldoDeudor = saldoDeudor;
        FechaUltimaCompra = fechaUltimaCompra;
        Observaciones = observaciones;
    }

    public int getIdDeudor() {
        return IdDeudor;
    }

    public void setIdDeudor(int idDeudor) {
        IdDeudor = idDeudor;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
    }

    public double getSaldoDeudor() {
        return SaldoDeudor;
    }

    public void setSaldoDeudor(double saldoDeudor) {
        SaldoDeudor = saldoDeudor;
    }

    public String getFechaUltimaCompra() {
        return FechaUltimaCompra;
    }

    public void setFechaUltimaCompra(String fechaUltimaCompra) {
        FechaUltimaCompra = fechaUltimaCompra;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public void setObservaciones(String observaciones) {
        Observaciones = observaciones;
    }

    public String getData(){
        return Nombre + " " +Apellidos;
    }
}
