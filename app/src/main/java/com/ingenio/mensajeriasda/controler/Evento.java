package com.ingenio.mensajeriasda.controler;

public class Evento {

    String fecha;
    String tipo;
    String curso;
    String detalle;
    String hora;
    String nota;
    String responsable;

    public Evento(String fecha, String tipo, String curso, String detalle, String hora, String nota, String responsable) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.curso = curso;
        this.detalle = detalle;
        this.hora = hora;
        this.nota = nota;
        this.responsable = responsable;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
