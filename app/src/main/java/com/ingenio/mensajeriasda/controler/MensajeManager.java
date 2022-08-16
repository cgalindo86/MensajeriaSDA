package com.ingenio.mensajeriasda.controler;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ingenio.mensajeriasda.MainActivity;
import com.ingenio.mensajeriasda.R;
import com.ingenio.mensajeriasda.model.Alumno;
import com.ingenio.mensajeriasda.model.Mensaje;
import com.ingenio.mensajeriasda.model.MensajeModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MensajeManager extends AppCompatActivity {

    ImageView atras;
    Button sincronizar;
    String elegido="", ppff="";
    TextView datosincronizacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mensaje_manager);
        //Toast.makeText(getApplicationContext(),isConnected+"01",Toast.LENGTH_LONG).show();
        final MensajeModel mensajeModel = new MensajeModel();

        final Alumno alumno = new Alumno();
        ppff = alumno.getPPFFDni(getApplicationContext());
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String[] valores = alumno.getAlumnosNombre(getApplicationContext()).split("_");
        final String[] valores2 = alumno.getAlumnos(getApplicationContext()).split("_");
        spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.itemspinner, valores));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                elegido = valores2[position];
                alumno.setAlumnoElegido(elegido,getApplicationContext());
                String ruta = "http://sdavirtualroom.dyndns.org/sda/controler/consultaAlumno.php?accionget=14"+
                        "&alumnoget="+elegido+"&ppffget="+ppff;
                Log.e("laruta",ruta);
                LeeAlumno leeAlumno = new LeeAlumno();
                leeAlumno.execute(ruta);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                //elegido = valores2[0];
            }
        });

        atras = (ImageView) findViewById(R.id.atras);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(MensajeManager.this,MainActivity.class);
                startActivity(i2);
            }
        });

        sincronizar = (Button) findViewById(R.id.sincronizar);
        sincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ruta = "http://sdavirtualroom.dyndns.org/sda/controler/consultaAlumno.php?accionget=14"+
                        "&alumnoget="+elegido+"&ppffget="+ppff;
                Log.e("laruta",ruta);
                LeeAlumno leeAlumno = new LeeAlumno();
                leeAlumno.execute(ruta);

            }
        });

        /*String ruta = "http://sdavirtualroom.dyndns.org/sda/controler/consultaAlumno.php?accionget=14"+
                "&alumnoget="+elegido+"&ppffget="+ppff;
        Log.e("laruta",ruta);
        LeeAlumno leeAlumno = new LeeAlumno();
        leeAlumno.execute(ruta);*/

    }

    public class LeeAlumno extends AsyncTask<String, Void, String> {

        ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {
            progressDoalog = new ProgressDialog(MensajeManager.this);
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

            String datAl = getDatos(dni);

            return datAl;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDoalog.dismiss();

            Log.e("resres",result);
            MensajeModel mensajeModel = new MensajeModel();
            mensajeModel.setMensaje(result,getApplicationContext());
            Lista2(result);
        }

    }

    private void Lista(String mensaje){
        final ListView lista = (ListView) findViewById(R.id.milista);
        Log.d("ingreso lista",mensaje);
        Alumno alumno = new Alumno();
        String elegido =  alumno.getAlumnoElegido(getApplicationContext());

        final ArrayList<Mensaje> arrayList = new ArrayList<Mensaje>();
        final String d[] = mensaje.split("#");

        if(mensaje!=""){

            int n = d.length;
            int i;
            for(i=0; i<d.length; i++){
                String e[] = d[i].split("%");
                //Log.e("d",e[4]+e[5]+elegido);
                Mensaje mensaje1 = new Mensaje(e[0],e[1],e[2],e[3],e[4],e[5],e[6],e[7],e[8],e[9]);
                arrayList.add(mensaje1);

            }
        }

        AdapterMensajes adapterMensajes = new AdapterMensajes(this,arrayList);
        lista.setAdapter(adapterMensajes);

    }

    private void Lista2(String mensaje){
        final ListView lista = (ListView) findViewById(R.id.milista);
        Log.d("ingreso lista",mensaje);
        Alumno alumno = new Alumno();
        String elegido =  alumno.getAlumnoElegido(getApplicationContext());

        final ArrayList<Mensaje> arrayList = new ArrayList<Mensaje>();
        final String d[] = mensaje.split("#");

        if(mensaje!=""){

            int n = d.length;
            int i;
            for(i=0; i<d.length; i++){
                String e[] = d[i].split("%");
                //Log.e("d",e[4]+e[5]+elegido);
                if(e[4].equals(elegido)){

                    Mensaje mensaje1 = new Mensaje(e[0],e[1],e[2],e[3],e[4],e[5],e[6],e[7],e[8],e[9]);
                    arrayList.add(mensaje1);
                }

            }
        }

        AdapterMensajes adapterMensajes = new AdapterMensajes(this,arrayList);
        lista.setAdapter(adapterMensajes);

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

    @Override
    public void onBackPressed (){
        Intent i = new Intent(MensajeManager.this, MainActivity.class);
        startActivity(i);
    }

}
