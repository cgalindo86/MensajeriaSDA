package com.ingenio.mensajeriasda;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ingenio.mensajeriasda.model.Alumno;
import com.ingenio.mensajeriasda.model.Eventos;
import com.ingenio.mensajeriasda.service.Conexion;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by cgali on 4/07/2017.
 */


public class InicioSesion extends Activity { //implements GoogleApiClient.OnConnectionFailedListener{

    private Button btnSignIn;

    private TextView txtNombre;
    private TextView txtEmail;
    private ImageView imagen;
    //private GoogleApiClient apiClient;
    private static final int RC_SIGN_IN = 1001;
    Boolean ev;
    private ProgressDialog progressDialog;
    String url = "http://sdavirtualroom.dyndns.org/sda";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_sesion);

        btnSignIn = (Button)findViewById(R.id.sign_in_button2);

        txtNombre = (TextView)findViewById(R.id.txtNombre);
        txtEmail = (TextView)findViewById(R.id.txtEmail);
        //imagen = (ImageView)findViewById(R.id.imagen);



        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText e = (EditText)findViewById(R.id.dnippff);
                String id = e.getText().toString();

                Alumno alumno = new Alumno();
                alumno.setPPFFDni(id,getApplicationContext());

                EditText e2 = (EditText)findViewById(R.id.dnialumno);
                String id2 = e2.getText().toString();

                //EditText p = (EditText)findViewById(R.id.pass);
                //String pass = p.getText().toString();
                String ruta =  url + "/consultaAlumno.php?id=" + id
                        +"&id2="+id2+"&opcion=2";
                Log.d("ruta",ruta);
                LeeAlumno lee = new LeeAlumno();
                lee.execute(ruta);

            }
        });

    }

    public class LeeAlumno extends AsyncTask<String, Void, String> {

        ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {
            progressDoalog = new ProgressDialog(InicioSesion.this);
            progressDoalog.setMax(100);
            progressDoalog.setMessage("Leyendo....");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String dni = params[0];
            String datAl = getAlumno(dni);
            Log.d("serv",datAl);
            if(datAl.equals("")){
                //DatAlumno("#&#&#&#&#&");
                ev=false;
            } else {
                ev = DatAlumno(datAl);
            }
            return ev+"";
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDoalog.dismiss();
            if (result.equals("true")){
                Conexion conexion = new Conexion();
                conexion.setUrl(url,getApplicationContext());
                Toast.makeText(getApplicationContext(), "INICIO CORRECTO", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(InicioSesion.this,MainActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "DEBE INICIAR SESIÓN CON UNA CUENTA SDA", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public String getAlumno(String entrada) {
        URL alumUrl = null;
        //Class<java.net.URL> aClass = java.net.URL.class;
        String url2="";
        try{
            alumUrl = new URL(entrada);
            HttpURLConnection conn = (HttpURLConnection) alumUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
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


    public Boolean DatAlumno(String datos2){
        String datos[]=datos2.split("#");
        Alumno alumno = new Alumno();
        alumno.setPPFF(datos[0],getApplicationContext());
        int i;
        String alumnos = "";
        String nombres = "";
        for(i=1; i< datos.length; i++){
            String datosServ[]=datos[i].split("&");
            alumnos = alumnos + datosServ[0]+"_";
            if(i==1){
                alumno.setAlumnoElegido(datosServ[0],getApplicationContext());
            }
            nombres = nombres + datosServ[1]+"_";
            alumno.setAlumnoData(datos[i],datosServ[0],getApplicationContext());

        }

        alumno.setAlumnos(alumnos,getApplicationContext());
        alumno.setAlumnosNombre(nombres,getApplicationContext());

        Eventos eventos = new Eventos();

        String d[] = alumno.getAlumnos(getApplicationContext()).split("_");
        int i2=0;
        for(i2=0; i2<d.length; i2++){
            eventos.setDatosMes("","01",d[i2],getApplicationContext());
            eventos.setDatosMes("","02",d[i2],getApplicationContext());
            eventos.setDatosMes("","03",d[i2],getApplicationContext());
            eventos.setDatosMes("","04",d[i2],getApplicationContext());
            eventos.setDatosMes("","05",d[i2],getApplicationContext());
            eventos.setDatosMes("","06",d[i2],getApplicationContext());
            eventos.setDatosMes("","07",d[i2],getApplicationContext());
            eventos.setDatosMes("","08",d[i2],getApplicationContext());
            eventos.setDatosMes("","09",d[i2],getApplicationContext());
            eventos.setDatosMes("","10",d[i2],getApplicationContext());
            eventos.setDatosMes("","11",d[i2],getApplicationContext());
            eventos.setDatosMes("","12",d[i2],getApplicationContext());
        }

        return  true;

    }

    @Override
    public void onBackPressed (){

        Intent i = new Intent(InicioSesion.this, InicioSesion.class);
        startActivity(i);
    }

}