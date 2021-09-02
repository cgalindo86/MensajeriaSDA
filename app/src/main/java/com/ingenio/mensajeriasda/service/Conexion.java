package com.ingenio.mensajeriasda.service;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by cgali on 26/06/2017.
 */

public class Conexion {


    public Conexion(){

    }

    public String getIP(){
        List<InetAddress> addrs;
        String address = "";
        try{
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for(NetworkInterface intf : interfaces){
                addrs = Collections.list(intf.getInetAddresses());
                for(InetAddress addr : addrs){
                    if(!addr.isLoopbackAddress() && addr instanceof Inet4Address){
                        address = addr.getHostAddress().toUpperCase(new Locale("es", "MX"));
                    }
                }
            }
        }catch (Exception e){
            Log.w(TAG, "Ex getting IP value " + e.getMessage());
        }
        return address;
    }

    public String ComprobarConexion(String url, Context context){
        URL miUrl=null;
        String conectado="";
        //Log.d("Comprobar",url+"+"+context);
        try{
            String url2=url;
            miUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) miUrl.openConnection();
            String codigoRespuesta = Integer.toString(conn.getResponseCode());
            if(codigoRespuesta.equals("200")){//Vemos si es 200 OK y leemos el cuerpo del mensaje.
                conectado=url2;
                setUrl(url2,context);

            } else {
                //setEstado("No conectado",context);
                setUrl("http://10.10.0.79/sda",context);
                conectado=getUrlExterna(context);
            }
        }catch(IOException ex){
            //setEstado("No conectado",context);
            setUrl("http://10.10.0.79/sda",context);
            conectado=getUrlExterna(context);
            ex.printStackTrace();
        }
        return conectado;
    }

    public String getUrl(Context context) {
        String url2="";
        try {

            FileInputStream fin = context.openFileInput("conexion_url.txt");
            int size;
            String url = "";

            while ((size = fin.read()) != -1) {
                url += Character.toString((char) size);
            }
            url2=url;
            //Log.d("url dentro",url);
        } catch (IOException io) {
            io.printStackTrace();
        }
        //Log.d("url2",url2);
        return url2;
    }

    public void setUrl(String url, Context context) {

        try{
            //Log.d("url recibida",url);
            String url2=url;
            FileOutputStream fout1 = context.openFileOutput("conexion_url.txt",MODE_PRIVATE);
            fout1.write(url2.getBytes());
            fout1.close();
        } catch (IOException ioe){}
    }



    public String getEstado(Context context) {
        String estado2="";
        try {

            FileInputStream fin = context.openFileInput("conexion_estado.txt");
            int size;
            String estado = "";

            while ((size = fin.read()) != -1) {
                estado += Character.toString((char) size);
            }
            estado2=estado;
            //Log.d("estado dentro",estado);
        } catch (IOException io) {
            io.printStackTrace();
        }
        //Log.d("estado2",estado2);
        return estado2;
    }

    public String getUrlInterna(Context context) {
        String url2="";
        try {

            FileInputStream fin = context.openFileInput("url_interna.txt");
            int size;
            String url = "";

            while ((size = fin.read()) != -1) {
                url += Character.toString((char) size);
            }
            url2=url;
            //Log.d("estado dentro",estado);
        } catch (IOException io) {
            io.printStackTrace();
        }
        //Log.d("estado2",estado2);
        return url2;
    }

    public String getUrlExterna(Context context) {
        String url2="";
        try {

            FileInputStream fin = context.openFileInput("url_externa.txt");
            int size;
            String url = "";

            while ((size = fin.read()) != -1) {
                url += Character.toString((char) size);
            }
            url2=url;
            //Log.d("estado dentro",estado);
        } catch (IOException io) {
            io.printStackTrace();
        }
        //Log.d("estado2",estado2);
        return url2;
    }

    public void setEstado(String estado, Context context) {
        try{
            //Log.d("estado recibida",estado+"+"+context);
            String estado2=estado;
            FileOutputStream fout1 = context.openFileOutput("conexion_estado.txt",MODE_PRIVATE);
            fout1.write(estado2.getBytes());
            fout1.close();
        } catch (IOException ioe){}
    }

    public Boolean setUrlInterna(String url, Context context){
        try{
            //Log.d("url recibida",url);
            String url2=url;
            FileOutputStream fout1 = context.openFileOutput("url_interna.txt",MODE_PRIVATE);
            fout1.write(url2.getBytes());
            fout1.close();
        } catch (IOException ioe){}
        return true;
    }

    public Boolean setUrlExterna(String url, Context context){
        try{
            //Log.d("url recibida",url);
            String url2=url;
            FileOutputStream fout1 = context.openFileOutput("url_externa.txt",MODE_PRIVATE);
            fout1.write(url2.getBytes());
            fout1.close();
        } catch (IOException ioe){}
        return true;
    }

    public Boolean modificaUrlInterna(String url, Context context){
        try{
            //Log.d("url recibida",url);
            String url2=url;
            FileOutputStream fout1 = context.openFileOutput("url_interna.txt",MODE_PRIVATE);
            fout1.write(url2.getBytes());
            fout1.close();
        } catch (IOException ioe){}
        return true;
    }

    public Boolean modificaUrlExterna(String url, Context context){
        try{
            //Log.d("url recibida",url);
            String url2=url;
            FileOutputStream fout1 = context.openFileOutput("url_externa.txt",MODE_PRIVATE);
            fout1.write(url2.getBytes());
            fout1.close();
        } catch (IOException ioe){}
        return true;
    }
}
