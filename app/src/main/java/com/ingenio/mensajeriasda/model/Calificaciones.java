package com.ingenio.mensajeriasda.model;

public class Calificaciones {

    String item;
    String nota;
    String color;

    public Calificaciones(String item, String nota, String color) {
        this.item = item;
        this.nota = nota;
        this.color = color;
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

    public String getcolor() {
        return color;
    }

    public void setcolor(String color) {
        this.color = color;
    }
}
