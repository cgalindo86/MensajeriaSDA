package com.ingenio.mensajeriasda.controler;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.WriterException;
import com.ingenio.mensajeriasda.InicioSesion;
import com.ingenio.mensajeriasda.MainActivity;
import com.ingenio.mensajeriasda.model.Alumno;
import com.ingenio.mensajeriasda.R;
import com.ingenio.mensajeriasda.service.Conexion;
import com.ingenio.mensajeriasda.service.ConsultasBD;
import com.ingenio.mensajeriasda.service.Imagenes;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class MiQr extends AppCompatActivity {

    // variables for imageview, edittext,
    // button, bitmap and qrencoder.
    private ImageView qrCodeIV,qrCodeIV2;
    private EditText dataEdt;
    private Button generateQrBtn;
    Bitmap bitmap,bitmap2;
    QRGEncoder qrgEncoder,qrgEncoder2;
    TextView nombre,dni,gradoseccion,nombre2,dni2,contacto;
    Spinner spinner;
    String elegido="",nombreElegido="", grado="", seccion="", edni="";
    int cont1=0;
    ImageView fotoalumno,fotoppff;
    Chronometer cronometer;
    Date date;
    DateFormat dateFormat,hourFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.miqr2);

        cronometer = (Chronometer) findViewById(R.id.crono);
        cronometer.setFormat("Tiempo: 00:%s");
        cronometer.setBase(SystemClock.elapsedRealtime());

        //time1 = SystemClock.elapsedRealtime();

        fotoalumno = (ImageView) findViewById(R.id.fotoalumno);
        fotoppff = (ImageView) findViewById(R.id.fotoppff);
        fotoalumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ruta4 = "http://sdavirtualroom.dyndns.org/sda/view/fotoAlumno.php?alumno="+
                        elegido+"&grado="+grado+"&seccion="+seccion;

                Intent intent4 = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(ruta4));
                startActivity(intent4);

            }
        });

        fotoppff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ruta4 = "http://sdavirtualroom.dyndns.org/sda/view/fotoPPFF.php?alumno="+
                        elegido+"&grado="+grado+"&seccion="+seccion+"&ppff="+edni;

                Intent intent4 = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(ruta4));
                startActivity(intent4);

            }
        });

        final Alumno alumno = new Alumno();
        spinner = (Spinner) findViewById(R.id.spinner);
        String[] valores = alumno.getAlumnosNombre(getApplicationContext()).split("_");
        final String[] valores2 = alumno.getAlumnos(getApplicationContext()).split("_");
        spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.itemspinner, valores));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                nombreElegido = valores[position];
                elegido = valores2[position];
                alumno.setAlumnoElegido(elegido,getApplicationContext());
                GenerarCarnet();
                //alumno.setAlumnoElegido(elegido,getApplicationContext());
                Log.e("eee",alumno.getAlumnoData(elegido,getApplicationContext()));
                String gr[] = alumno.getAlumnoData(elegido,getApplicationContext()).split("&");
                grado = gr[2]; seccion = gr[3];

                final String uurl="http://sdavirtualroom.dyndns.org/sda/pizarra/"+
                        "public/imagenes/fotocheck/"+grado+"/"+seccion+"/"+elegido+"/"+elegido+".png";
                Log.e("restaurar",uurl);
                ExtraerFondo3 extraerFondo = new ExtraerFondo3();
                extraerFondo.execute(uurl);

                final String uurl2="http://sdavirtualroom.dyndns.org/sda/pizarra/"+
                        "public/imagenes/fotocheck/"+grado+"/"+seccion+"/"+edni+"/"+edni+".png";
                Log.e("restaurar",uurl2);
                ExtraerFondo4 extraerFondo4 = new ExtraerFondo4();
                extraerFondo4.execute(uurl2);

                String uurl3="http://sdavirtualroom.dyndns.org/sda/controler/consultaAlumno.php?accionget=-1";
                Log.e("crono",uurl3);
                LeeAlumno leeAlumno = new LeeAlumno();
                leeAlumno.execute(uurl3);
                cronometer.start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                //elegido = valores2[0];
            }
        });




    }

    void GenerarCarnet(){
        nombre = (TextView) findViewById(R.id.enombre);
        gradoseccion = (TextView) findViewById(R.id.grado);
        dni = (TextView) findViewById(R.id.edni);
        nombre2 = (TextView) findViewById(R.id.enombre2);
        contacto = (TextView) findViewById(R.id.contacto);
        dni2 = (TextView) findViewById(R.id.edni2);

        Alumno alumno = new Alumno();
        String d[] = alumno.getAlumnoData(elegido,getApplicationContext()).split("&");
        String grado = d[2];
        String seccion = d[3];
        //81172225&PEZO GONZALES, Kent Tizianno&P3&A&17&
        //Log.e("dni",profesor.getId_DNI(getApplicationContext()));
        nombre.setText(nombreElegido);
        gradoseccion.setText(""+grado+" - "+seccion);
        dni.setText("DNI: " +elegido);

        Log.e("data ppff",alumno.getPPFF(getApplicationContext()));
        String p[] = alumno.getPPFF(getApplicationContext()).split("&");
        String enombre = p[0];
        String econtacto = p[1]+" - "+p[2];
        edni = alumno.getPPFFDni(getApplicationContext());

        nombre2.setText(enombre);
        contacto.setText("");
        dni2.setText("DNI: " +edni);

        final String uurl="http://sdavirtualroom.dyndns.org/sda/pizarra/"+
                "public/imagenes/fotocheck/"+grado+"/"+seccion+"/"+edni+"/"+edni+".png";
        Log.e("restaurar",uurl);
        ExtraerFondo4 extraerFondo4 = new ExtraerFondo4();
        extraerFondo4.execute(uurl);

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        qrCodeIV = (ImageView) findViewById(R.id.idIVQrcode);
        qrCodeIV2 = (ImageView) findViewById(R.id.idIVQrcode2);
        // initializing a variable for default display.
        Display display = manager.getDefaultDisplay();

        // creating a variable for point which
        // is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);

        // getting width and
        // height of a point
        int width = point.x;
        int height = point.y;

        // generating dimension from width and height.
        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

        //Profesor profesor = new Profesor();
        //String midni = profesor.getId_DNI(getApplicationContext());
        Log.e("midni",elegido);

        // setting this dimensions inside our qr code
        // encoder to generate our qr code.
        qrgEncoder = new QRGEncoder(elegido, null, QRGContents.Type.TEXT, dimen);
        try {
            // getting our qrcode in the form of bitmap.
            bitmap = qrgEncoder.encodeAsBitmap();
            // the bitmap is set inside our image
            // view using .setimagebitmap method.
            qrCodeIV.setImageBitmap(bitmap);
        } catch (WriterException e) {
            // this method is called for
            // exception handling.
            Log.e("Tag", e.toString());
        }

        qrgEncoder2 = new QRGEncoder(edni, null, QRGContents.Type.TEXT, dimen);
        try {
            // getting our qrcode in the form of bitmap.
            bitmap2 = qrgEncoder2.encodeAsBitmap();
            // the bitmap is set inside our image
            // view using .setimagebitmap method.
            qrCodeIV2.setImageBitmap(bitmap2);
        } catch (WriterException e) {
            // this method is called for
            // exception handling.
            Log.e("Tag", e.toString());
        }
    }

    public class ExtraerFondo3 extends AsyncTask<String,Void,Bitmap> {

        ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {
            progressDoalog = new ProgressDialog(MiQr.this);
            progressDoalog.setMax(100);
            progressDoalog.setMessage("Leyendo....");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub
            String dni = params[0];
            Imagenes imagenes = new Imagenes();
            Bitmap mbitmap = imagenes.getContenido(dni);

            return mbitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDoalog.dismiss();
            if(result!=null){
                fotoalumno.setImageBitmap(result);
            } else {
                fotoalumno.setImageResource(R.drawable.imagen);
                //Toast.makeText(getApplicationContext(),"No se encuentra imagen",Toast.LENGTH_LONG).show();
            }

        }

    }

    public class ExtraerFondo4 extends AsyncTask<String,Void,Bitmap> {

        ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {
            progressDoalog = new ProgressDialog(MiQr.this);
            progressDoalog.setMax(100);
            progressDoalog.setMessage("Leyendo....");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub
            String dni = params[0];
            Imagenes imagenes = new Imagenes();
            Bitmap mbitmap = imagenes.getContenido(dni);

            return mbitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDoalog.dismiss();
            if(result!=null){
                fotoppff.setImageBitmap(result);
            } else {
                fotoppff.setImageResource(R.drawable.imagen);
                //Toast.makeText(getApplicationContext(),"No se encuentra imagen",Toast.LENGTH_LONG).show();
            }

        }

    }

    @Override
    public void onResume(){
        super.onResume();
        final String uurl="http://sdavirtualroom.dyndns.org/sda/pizarra/"+
                "public/imagenes/fotocheck/"+grado+"/"+seccion+"/"+elegido+"/"+elegido+".png";
        Log.e("restaurar",uurl);
        ExtraerFondo3 extraerFondo3 = new ExtraerFondo3();
        extraerFondo3.execute(uurl);

        final String uurl2="http://sdavirtualroom.dyndns.org/sda/pizarra/"+
                "public/imagenes/fotocheck/"+grado+"/"+seccion+"/"+edni+"/"+edni+".png";
        Log.e("restaurar",uurl2);
        ExtraerFondo4 extraerFondo4 = new ExtraerFondo4();
        extraerFondo4.execute(uurl2);
    }

    public class LeeAlumno extends AsyncTask<String, Void, String> {

        ProgressDialog progressDoalog;
        @Override
        protected void onPreExecute() {
            progressDoalog = new ProgressDialog(MiQr.this);
            progressDoalog.setMax(100);
            progressDoalog.setMessage("Leyendo....");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String dni = params[0];
            ConsultasBD consultasBD = new ConsultasBD();
            String datAl = consultasBD.getDatos(dni);
            Log.e("consulta",dni+""+datAl);
            return datAl;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDoalog.dismiss();
            String d[] = result.split("___");
            TextView fecha = (TextView) findViewById(R.id.fecha);
            fecha.setText(d[0]);
            TextView hora = (TextView) findViewById(R.id.hora);
            hora.setText(d[1]);

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
        Log.e("Consulta",url2);
        return url2;
    }
}
