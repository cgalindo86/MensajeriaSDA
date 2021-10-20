package com.ingenio.mensajeriasda.controler;

public class Calificaciones {

    String item;
    String nota;
    String tipo;

    public Calificaciones(String item, String nota, String tipo) {
        this.item = item;
        this.nota = nota;
        this.tipo = tipo;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
