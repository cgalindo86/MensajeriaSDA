package com.ingenio.mensajeriasda.controler;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.ingenio.mensajeriasda.MainActivity;
import com.ingenio.mensajeriasda.R;
import com.ingenio.mensajeriasda.model.Alumno;
import com.ingenio.mensajeriasda.model.MensajeModel;
import com.ingenio.mensajeriasda.model.Pago;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PagosManager extends AppCompatActivity {

    ImageView atras;
    String elegido="";
    String ruta="", anio="2022";
    Button boton,boton2,b2022,b2021,b2020,b2019;
    int ancho,alto,medida,medida2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagos_manager);

        boton = (Button) findViewById(R.id.comopagar);
        boton2 = (Button) findViewById(R.id.pagar);
        b2022 = (Button) findViewById(R.id.b2022);
        b2021 = (Button) findViewById(R.id.b2021);

        Display display = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        ancho = point.x; alto = point.y;
        if(ancho>alto){
            medida = ancho/2 - 20;
        } else {
            medida = ancho/2 - 20;
        }
        ViewGroup.LayoutParams params1 = boton.getLayoutParams();
        params1.width = medida;
        boton.setLayoutParams(params1);
        boton2.setLayoutParams(params1);

        if(ancho>alto){
            medida2 = ancho/2;
        } else {
            medida2 = ancho/2;
        }
        ViewGroup.LayoutParams params2 = b2022.getLayoutParams();
        params2.width = medida2;
        b2022.setLayoutParams(params2);
        b2021.setLayoutParams(params2);
        //b2020.setLayoutParams(params2);
        //b2019.setLayoutParams(params2);


        //Toast.makeText(getApplicationContext(),isConnected+"01",Toast.LENGTH_LONG).show();
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
                //Lista2(mensajeModel.getMensaje(getApplicationContext()));
                ruta = "http://sdavirtualroom.dyndns.org/sda/controler/consultaAlumno.php?accionget=3&alumnoget="+elegido+"&anioget="+anio;

                LeeDatos leeDatos = new LeeDatos();
                leeDatos.execute(ruta);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                //elegido = valores2[0];
            }
        });


        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ruta = "http://sdavirtualroom.dyndns.org/sda/controler/files/flayer.jpeg";

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(ruta));
                startActivity(intent);
            }
        });


        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ruta = "https://pagolink.niubiz.com.pe/pagoseguro/AsociacionEducativaSantoDomingoelApostol/980560";

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(ruta));
                startActivity(intent);
            }
        });

        b2022.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Limpiar();
                b2022.setBackground(getResources().getDrawable(R.drawable.bordec1));
                anio="2022";
                ruta = "http://sdavirtualroom.dyndns.org/sda/controler/consultaAlumno.php?accionget=3&alumnoget="+elegido+"&anioget="+anio;

                LeeDatos leeDatos = new LeeDatos();
                leeDatos.execute(ruta);
            }
        });

        b2021.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Limpiar();
                b2021.setBackground(getResources().getDrawable(R.drawable.bordec1));
                anio="2021";
                ruta = "http://sdavirtualroom.dyndns.org/sda/controler/consultaAlumno.php?accionget=3&alumnoget="+elegido+"&anioget="+anio;

                LeeDatos leeDatos = new LeeDatos();
                leeDatos.execute(ruta);
            }
        });



        atras = (ImageView) findViewById(R.id.atras);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(PagosManager.this,MainActivity.class);
                startActivity(i2);
            }
        });



        Lista(mensajeModel.getMensaje(getApplicationContext()));

    }

    void Limpiar(){
        b2022.setBackground(getResources().getDrawable(R.drawable.bordec2));
        b2021.setBackground(getResources().getDrawable(R.drawable.bordec2));
        //b2020.setBackground(getResources().getDrawable(R.drawable.bordec2));
        //b2019.setBackground(getResources().getDrawable(R.drawable.bordec2));
    }

    public class LeeDatos extends AsyncTask<String,Void,String> {

        ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {
            progressDoalog = new ProgressDialog(PagosManager.this);
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

    private void Lista(String mensaje){
        final ListView lista = (ListView) findViewById(R.id.milista);
        Log.d("ingreso lista",mensaje);
        //Alumno alumno = new Alumno();
        //String elegido =  alumno.getAlumnoElegido(getApplicationContext());

        final ArrayList<Pago> arrayList = new ArrayList<Pago>();
        final String d[] = mensaje.split("#");

        if(mensaje!=""){

            int n = d.length;
            int i;
            for(i=0; i<d.length; i++){
                String e[] = d[i].split("%");
                //Log.e("d",e[4]+e[5]+elegido);
                Pago pago = new Pago(e[0],e[1],e[2]);
                //Mensaje mensaje1 = new Mensaje(e[0],e[1],e[2],e[3],e[4],e[5],e[6],e[7],e[8],e[9]);
                arrayList.add(pago);

            }
        }

        AdapterPagos adapterPagos = new AdapterPagos(this,arrayList);
        lista.setAdapter(adapterPagos);

    }

    @Override
    public void onBackPressed (){
        Intent i = new Intent(PagosManager.this, MainActivity.class);
        startActivity(i);
    }

}
