package com.ingenio.mensajeriasda.model;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by cgali on 19/12/2017.
 */

public class MensajeModel {

    public MensajeModel(){}

    public String getMensaje(Context context) {
        String nombre2="";
        try {

            FileInputStream fin = context.openFileInput("dataMensaje.txt");
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
            nombre2="";
            //setMensaje("",context);
        }
        return nombre2;
    }

    public void setMensaje(String nombre_alum, Context context) {
        try{
            String nombre2=nombre_alum;
            FileOutputStream fout1 = context.openFileOutput("dataMensaje.txt",MODE_PRIVATE);
            fout1.write(nombre2.getBytes());
            fout1.close();
        } catch (IOException ioe){}
    }

    public String getMensajeElegido(Context context) {
        String nombre2="";
        try {

            FileInputStream fin = context.openFileInput("dataMensajeElegido.txt");
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
            nombre2="";
            //setMensajeElegido("",context);
        }
        return nombre2;
    }

    public void setMensajeElegido(String nombre_alum, Context context) {
        try{
            String nombre2=nombre_alum;
            FileOutputStream fout1 = context.openFileOutput("dataMensajeElegido.txt",MODE_PRIVATE);
            fout1.write(nombre2.getBytes());
            fout1.close();
        } catch (IOException ioe){}
    }

    public String getMensajeNotificacion(Context context) {
        String nombre2="";
        try {

            FileInputStream fin = context.openFileInput("dataMensajeNotificacion.txt");
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
            nombre2="";
            //setMensajeElegido("",context);
        }
        return nombre2;
    }

    public void setMensajeNotificacion(String nombre_alum, Context context) {
        try{
            String nombre2=nombre_alum;
            FileOutputStream fout1 = context.openFileOutput("dataMensajeNotificacion.txt",MODE_PRIVATE);
            fout1.write(nombre2.getBytes());
            fout1.close();
        } catch (IOException ioe){}
    }

}
