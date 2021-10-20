package com.ingenio.mensajeriasda.controler;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.github.nkzawa.socketio.client.Socket;
import com.ingenio.mensajeriasda.BootReceiver;
import com.ingenio.mensajeriasda.MainActivity;
import com.ingenio.mensajeriasda.R;
import com.ingenio.mensajeriasda.model.Alumno;
import com.ingenio.mensajeriasda.model.Eventos;
import com.ingenio.mensajeriasda.model.MensajeModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CalificacionesManager extends AppCompatActivity {

    ImageView atras;
    String elegido="", bimestre="1", curso="", grado="";
    String ruta="";
    LinearLayout linearLayout;
    Button boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calificaciones_manager);
        //Toast.makeText(getApplicationContext(),isConnected+"01",Toast.LENGTH_LONG).show();

        linearLayout = (LinearLayout) findViewById(R.id.calificaciones);
        linearLayout.setVisibility(View.GONE);

        boton = (Button) findViewById(R.id.ocultar);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.setVisibility(View.GONE);
            }
        });

        final MensajeModel mensajeModel = new MensajeModel();

        final Alumno alumno = new Alumno();
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
                Log.e("eee",alumno.getAlumnoData(elegido,getApplicationContext()));
                String gr[] = alumno.getAlumnoData(elegido,getApplicationContext()).split("&");
                grado=gr[2];
                ruta = "http://sdavirtualroom.dyndns.org/sda/controler/consultaAlumno.php?accionget=4&gradoget="+grado;

                LeeDatos leeDatos = new LeeDatos();
                leeDatos.execute(ruta);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                //elegido = valores2[0];
            }
        });

        Spinner spinner0 = (Spinner) findViewById(R.id.spinner0);
        String bimestres[] = {"1er Bimestre","2do Bimestre","3er Bimestre","4to Bimestre"};
        final String bimestres2[] = {"1","2","3","4"};
        spinner0.setAdapter(new ArrayAdapter<String>(this, R.layout.itemspinner, bimestres));
        spinner0.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                bimestre = bimestres2[position];
                linearLayout.setVisibility(View.VISIBLE);

                Alumno alumno = new Alumno();
                String elegido = alumno.getAlumnoElegido(getApplicationContext());
                ruta = "http://sdavirtualroom.dyndns.org/sda/controler/files/"+
                        "archivosApp"+grado+".php?alumno="+elegido+"&curso="+curso+"&bimestre="+bimestre;

                LeeDatos2 leeDatos2 = new LeeDatos2();
                leeDatos2.execute(ruta);
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
                Intent i2 = new Intent(CalificacionesManager.this,MainActivity.class);
                startActivity(i2);
            }
        });



        //Lista(mensajeModel.getMensaje(getApplicationContext()));

    }

    public class LeeDatos extends AsyncTask<String,Void,String> {

        ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {
            progressDoalog = new ProgressDialog(CalificacionesManager.this);
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
            Log.e("ruta",dni);

            String ladata = getDatos(dni);
            return ladata;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDoalog.dismiss();
            Lista(result);

        }

    }

    public String getDatos(String entrada) {
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



    public class LeeDatos2 extends AsyncTask<String,Void,String> {

        ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {
            progressDoalog = new ProgressDialog(CalificacionesManager.this);
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
            Log.e("ruta",dni);

            String ladata = getDatos(dni);
            return ladata;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDoalog.dismiss();
            Lista2(result);

        }

    }

    private void Lista(String mensaje){
        final ListView lista = (ListView) findViewById(R.id.milista);
        Log.d("ingreso lista",mensaje);
        //Alumno alumno = new Alumno();
        //String elegido =  alumno.getAlumnoElegido(getApplicationContext());

        final ArrayList<Pago> arrayList = new ArrayList<Pago>();
        final String d[] = mensaje.split("___");

        if(mensaje!=""){

            int n = d.length;
            int i;
            for(i=0; i<d.length; i++){
                String e[] = d[i].split("%");
                //Log.e("d",e[4]+e[5]+elegido);
                Pago pago = new Pago(e[0],"","");
                //Mensaje mensaje1 = new Mensaje(e[0],e[1],e[2],e[3],e[4],e[5],e[6],e[7],e[8],e[9]);
                arrayList.add(pago);

            }
        }

        AdapterPagos adapterPagos = new AdapterPagos(this,arrayList);
        lista.setAdapter(adapterPagos);
        lista.setClickable(true);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                //Object o = listView.getItemAtPosition(position);
                // Realiza lo que deseas, al recibir clic en el elemento de tu listView determinado por su posicion.
                Log.e("Click", "click en el elemento " + position + " de mi ListView");
                Log.e("click2",d[position] +"");
                String e[] = d[position].split("%");
                curso = e[1];
                //curso = curso.replace(" ","%20");
                linearLayout.setVisibility(View.VISIBLE);

                Alumno alumno = new Alumno();
                String elegido = alumno.getAlumnoElegido(getApplicationContext());
                ruta = "http://sdavirtualroom.dyndns.org/sda/controler/files/"+
                        "archivosApp"+grado+".php?alumno="+elegido+"&curso="+curso+"&bimestre="+bimestre;

                LeeDatos2 leeDatos2 = new LeeDatos2();
                leeDatos2.execute(ruta);
            }
        });
    }

    private void Lista2(String mensaje){
        final ListView lista = (ListView) findViewById(R.id.milista0);
        Log.e("ingreso lista",mensaje);
        final ArrayList<Calificaciones> arrayList = new ArrayList<Calificaciones>();
        final String d[] = mensaje.split("____");
        if(mensaje!=""){
            int i;
            int n = d.length;

            for(i=0; i<n; i++){
                Log.e("ii",d[i]);
                String e[] = d[i].split("___");
                Log.e("ii2",e[0]);
                String data[] = e[0].split(": ");
                if(data.length>1){
                    Calificaciones calificaciones = new Calificaciones(data[0],data[1],"1");
                    arrayList.add(calificaciones);
                } else {
                    Calificaciones calificaciones = new Calificaciones(data[0],"","1");
                    arrayList.add(calificaciones);
                }


                Log.e("competencia",e[0]);
                String f[] = e[1].split("_");
                int i2=0;
                String ladata="";
                for(i2=0; i2<f.length; i2++){
                    String lad = f[i2];
                    String data2[] = f[i2].split(": ");
                    if(!ladata.equals(data2[0])){
                        if(data2.length>1){
                            Calificaciones calificaciones = new Calificaciones(data2[0],data2[1],"2");
                            arrayList.add(calificaciones);
                        } else {
                            Calificaciones calificaciones = new Calificaciones(data2[0],"","2");
                            arrayList.add(calificaciones);
                        }
                    }
                    ladata = data2[0];
                    //Calificaciones calificaciones2 = new Calificaciones(data2[0],data2[1],"2");
                    Log.e("capacidad",f[i2]);
                    //arrayList.add(calificaciones2);
                }
            }

        }

        AdapterCalificaciones adapterCalificaciones = new AdapterCalificaciones(this,arrayList);
        lista.setAdapter(adapterCalificaciones);

    }

    public static boolean esMayuscula(String s) {
        // Regresa el resultado de comparar la original con su versión mayúscula
        return s.equals(s.toUpperCase());
    }

    public static boolean esMinuscula(String s) {
        // Regresa el resultado de comparar la original con su versión minúscula
        return s.equals(s.toLowerCase());
    }

    @Override
    public void onBackPressed (){
        Intent i = new Intent(CalificacionesManager.this, MainActivity.class);
        startActivity(i);
    }

}
