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

public class Eventos {

    public Eventos(){}

    public String getDatosMes(String fecha, String alumno, Context context) {
        String pass2="";
        try {

            FileInputStream fin = context.openFileInput("eventos_mes_"+fecha+"_"+alumno+".txt");
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

    public void setDatosMes(String datos, String fecha, String alumno,Context context) {
        try{
            String pass2=datos;
            FileOutputStream fout1 = context.openFileOutput("eventos_mes_"+fecha+"_"+alumno+".txt",MODE_PRIVATE);
            fout1.write(pass2.getBytes());
            fout1.close();
        } catch (IOException ioe){}
    }

    public String getDatosFecha(String datos, String fecha, Context context) {
        String pass2="";

        return pass2;
    }

    public void setDatosFecha(String datos, String fecha, Context context) {
        try{
            String pass2=datos;
            FileOutputStream fout1 = context.openFileOutput("eventos_fecha_"+fecha+".txt",MODE_PRIVATE);
            fout1.write(pass2.getBytes());
            fout1.close();
        } catch (IOException ioe){}
    }

}
