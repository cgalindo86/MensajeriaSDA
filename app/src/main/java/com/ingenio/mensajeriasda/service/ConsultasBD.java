package com.ingenio.mensajeriasda.service;

/**
 * Created by cgali on 5/07/2017.
 */
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConsultasBD {

    public ConsultasBD(){}

    public String Consulta(String url){
        URL miUrl=null;
        String conectado="";
        String codigoRespuesta="";
        //Log.d("Comprobar",url+"+"+context);
        try{
            //String url2=url;
            miUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) miUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();
            codigoRespuesta = Integer.toString(conn.getResponseCode());
            if(codigoRespuesta.equals("200")){//Vemos si es 200 OK y leemos el cuerpo del mensaje.
                conectado="ok";

            } else {
                conectado="no ok";

            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
        return codigoRespuesta;
    }

    public String getDatos(String entrada) {
        URL alumUrl = null;
        String url2="";
        try{
            alumUrl = new URL(entrada);
            HttpURLConnection conn = (HttpURLConnection) alumUrl.openConnection();
            //conn.setRequestMethod("GET");
            //conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            byte[] array = new byte[1000]; // buffer temporal de lectura.
            StringBuffer out = new StringBuffer();
            byte[] b = new byte[4096];
            for (int n; (n = is.read(b)) != -1;) {
                out.append(new String(b, 0, n, "UTF-8"));
            }
            String pot=new String(out.toString().getBytes("UTF-8"));
            url2=pot;
        }catch(IOException ex){
            ex.printStackTrace();
        }
        Log.d("Consulta",url2);
        return url2;
    }
}