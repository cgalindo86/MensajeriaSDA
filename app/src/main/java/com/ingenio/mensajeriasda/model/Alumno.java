package com.ingenio.mensajeriasda.model;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by cgali on 4/07/2017.
 */

public class Alumno {

    public Alumno(){}

    public String getPPFF(Context context) {
        String pass2="";
        try {

            FileInputStream fin = context.openFileInput("alumno_ppff.txt");
            int size;
            byte[] array = new byte[1000]; // buffer temporal de lectura.
            StringBuffer out = new StringBuffer();
            byte[] b = new byte[4096];
            for (int n; (n = fin.read(b)) != -1;) {
                out.append(new String(b, 0, n, "UTF-8"));
            }
            String pot=new String(out.toString().getBytes("UTF-8"));
            pass2=pot;
            Log.d("nombre dentro",pass2);
        } catch (IOException io) {
            io.printStackTrace();
        }

        return pass2;
    }

    public void setPPFF(String pass, Context context) {
        try{
            String pass2=pass;
            FileOutputStream fout1 = context.openFileOutput("alumno_ppff.txt",MODE_PRIVATE);
            fout1.write(pass2.getBytes());
            fout1.close();
        } catch (IOException ioe){}
    }

    public String getPPFFDni(Context context) {
        String pass2="";
        try {

            FileInputStream fin = context.openFileInput("alumno_ppffDni.txt");
            int size;
            String pass = "";

            while ((size = fin.read()) != -1) {
                pass += Character.toString((char) size);
            }
            pass2=pass;

        } catch (IOException io) {
            io.printStackTrace();
        }
        return pass2;
    }

    public void setPPFFDni(String pass, Context context) {
        try{
            String pass2=pass;
            FileOutputStream fout1 = context.openFileOutput("alumno_ppffDni.txt",MODE_PRIVATE);
            fout1.write(pass2.getBytes());
            fout1.close();
        } catch (IOException ioe){}
    }

    public String getAlumnos(Context context) {
        String id_alumno2="";
        try {

            FileInputStream fin = context.openFileInput("alumno_alumnos.txt");
            int size;
            byte[] array = new byte[1000]; // buffer temporal de lectura.
            StringBuffer out = new StringBuffer();
            byte[] b = new byte[4096];
            for (int n; (n = fin.read(b)) != -1;) {
                out.append(new String(b, 0, n, "UTF-8"));
            }
            String pot=new String(out.toString().getBytes("UTF-8"));
            id_alumno2=pot;
            Log.d("nombre dentro",id_alumno2);
        } catch (IOException io) {
            io.printStackTrace();
        }
        return id_alumno2;
    }

    public void setAlumnos(String id_alumno, Context context) {
        try{
            String id2=id_alumno;
            FileOutputStream fout1 = context.openFileOutput("alumno_alumnos.txt",MODE_PRIVATE);
            fout1.write(id2.getBytes());
            fout1.close();
        } catch (IOException ioe){}
    }

    public String getAlumnosNombre(Context context) {
        String id_alumno2="";
        try {

            FileInputStream fin = context.openFileInput("alumno_alumnosNombre.txt");
            int size;
            byte[] array = new byte[1000]; // buffer temporal de lectura.
            StringBuffer out = new StringBuffer();
            byte[] b = new byte[4096];
            for (int n; (n = fin.read(b)) != -1;) {
                out.append(new String(b, 0, n, "UTF-8"));
            }
            String pot=new String(out.toString().getBytes("UTF-8"));
            id_alumno2=pot;
            Log.d("nombre dentro",id_alumno2);
        } catch (IOException io) {
            io.printStackTrace();
        }

        return id_alumno2;
    }

    public void setAlumnosNombre(String id_alumno, Context context) {
        try{
            String id2=id_alumno;
            FileOutputStream fout1 = context.openFileOutput("alumno_alumnosNombre.txt",MODE_PRIVATE);
            fout1.write(id2.getBytes());
            fout1.close();
        } catch (IOException ioe){}
    }

    public String getAlumnoElegido(Context context) {
        String id_alumno2="";
        try {

            FileInputStream fin = context.openFileInput("alumno_alumnoElegido.txt");
            int size;
            String id_alumno = "";

            while ((size = fin.read()) != -1) {
                id_alumno += Character.toString((char) size);
            }
            id_alumno2=id_alumno;
            Log.d("id_alumno dentro",id_alumno);
        } catch (IOException io) {
            io.printStackTrace();
        }
        return id_alumno2;
    }

    public void setAlumnoElegido(String id_alumno, Context context) {
        try{
            String id2=id_alumno;
            FileOutputStream fout1 = context.openFileOutput("alumno_alumnoElegido.txt",MODE_PRIVATE);
            fout1.write(id2.getBytes());
            fout1.close();
        } catch (IOException ioe){}
    }

    public String getAlumnoData(String codigo,Context context) {
        String nombre2="";
        try {

            FileInputStream fin = context.openFileInput("alumno_"+codigo+".txt");
            int size;
            byte[] array = new byte[1000]; // buffer temporal de lectura.
            StringBuffer out = new StringBuffer();
            byte[] b = new byte[4096];
            for (int n; (n = fin.read(b)) != -1;) {
                out.append(new String(b, 0, n, "UTF-8"));
            }
            String pot=new String(out.toString().getBytes("UTF-8"));
            nombre2=pot;
            Log.d("nombre dentro",nombre2);
        } catch (IOException io) {
            io.printStackTrace();
        }
        return nombre2;
    }

    public void setAlumnoData(String datos, String codigo, Context context) {
        try{
            String nombre2=datos;
            FileOutputStream fout1 = context.openFileOutput("alumno_"+codigo+".txt",MODE_PRIVATE);
            fout1.write(nombre2.getBytes());
            fout1.close();
        } catch (IOException ioe){}
    }


}
