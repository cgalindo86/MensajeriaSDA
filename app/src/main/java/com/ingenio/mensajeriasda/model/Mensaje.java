package com.ingenio.mensajeriasda.model;

import android.util.Log;

public class Mensaje {

    String supervisor;
    String supervisorMail;
    String supervisorCelular;
    String id_ppff;
    String alumno;
    String alumnoNombre;
    String fecha;
    String hora;
    String asunto;
    String curso;

    public Mensaje(String supervisor, String supervisorMail, String supervisorCelular, String id_ppff, String alumno, String alumnoNombre, String fecha, String hora, String asunto, String curso) {
        this.supervisor = supervisor;
        this.supervisorMail = supervisorMail;
        this.supervisorCelular = supervisorCelular;
        this.id_ppff = id_ppff;
        this.alumno = alumno;
        this.alumnoNombre = alumnoNombre;
        this.fecha = fecha;
        this.hora = hora;
        this.asunto = asunto;
        this.curso = curso;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getSupervisorMail() {
        return supervisorMail;
    }

    public void setSupervisorMail(String supervisorMail) {
        this.supervisorMail = supervisorMail;
    }

    public String getSupervisorCelular() {
        return supervisorCelular;
    }

    public void setSupervisorCelular(String supervisorCelular) {
        this.supervisorCelular = supervisorCelular;
    }

    public String getId_ppff() {
        return id_ppff;
    }

    public void setId_ppff(String id_ppff) {
        this.id_ppff = id_ppff;
    }

    public String getAlumno() {
        return alumno;
    }

    public void setAlumno(String alumno) {
        this.alumno = alumno;
    }

    public String getAlumnoNombre() {
        return alumnoNombre;
    }

    public void setAlumnoNombre(String alumnoNombre) {
        this.alumnoNombre = alumnoNombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }
}
