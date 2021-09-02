package com.ingenio.mensajeriasda.service;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by cgali on 19/06/2017.
 */

public class Imagenes {

    private Bitmap contenido;
    private String URL;


    public Imagenes() {
    }

    public Bitmap getContenido(String imageHttpAddress) {
        java.net.URL imageUrl = null;
        //Class<java.net.URL> aClass = java.net.URL.class;
        Bitmap imagen = null;
        try{
            imageUrl = new URL(imageHttpAddress);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            imagen = BitmapFactory.decodeStream(conn.getInputStream());
        }catch(IOException ex){
            ex.printStackTrace();
        }
        contenido=imagen;
        return contenido;
    }

    public void setContenido(Context context, String i, Bitmap imagen, String nombre) {
        ContextWrapper cw = new ContextWrapper(context);
        File dirImages = cw.getDir("Imagenes", Context.MODE_PRIVATE);
        nombre = nombre+"_p"+i+ ".png";
        File myPath = new File(dirImages, nombre);
        Log.d("nombre guardado",nombre);

        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(myPath);
            imagen.compress(Bitmap.CompressFormat.PNG, 10, fos);
            fos.flush();
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }catch (IOException ex){
            ex.printStackTrace();
        }
        this.contenido = imagen;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}