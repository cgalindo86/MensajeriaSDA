package com.ingenio.mensajeriasda.controler;

public class Pago {

    String mes;
    String estado;
    String importe;

    public Pago(String mes, String importe, String estado) {
        this.mes = mes;
        this.estado = estado;
        this.importe = importe;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }
}
