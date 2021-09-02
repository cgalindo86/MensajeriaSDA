package com.ingenio.mensajeriasda;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.ingenio.mensajeriasda.controler.AdapterAlumnos;
import com.ingenio.mensajeriasda.controler.AdapterMensajes;
import com.ingenio.mensajeriasda.controler.Alumnos;
import com.ingenio.mensajeriasda.controler.CalendarioManager;
import com.ingenio.mensajeriasda.controler.Mensaje;
import com.ingenio.mensajeriasda.controler.MensajeManager;
import com.ingenio.mensajeriasda.model.Alumno;
import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "com.ingenio.mensajeriasda";
    TextView txt;
    Button mensajes,calendario,virtualroom,cerrar;
    ListView listView;

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toast.makeText(getApplicationContext(),isConnected+"01",Toast.LENGTH_LONG).show();
        Log.i("inicio","inicio");

        Intent i2 = new Intent(MainActivity.this,MainActivity.class);
        BootReceiver bootReceiver = new BootReceiver();
        bootReceiver.onReceive(getApplicationContext(),i2);

        mensajes = (Button) findViewById(R.id.mensajes);
        calendario = (Button) findViewById(R.id.calendario);
        virtualroom = (Button) findViewById(R.id.virtualroom);
        cerrar = (Button) findViewById(R.id.cerrar);
        listView = (ListView) findViewById(R.id.milista);
        listView.setVisibility(View.GONE);

        mensajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(MainActivity.this,MensajeManager.class);
                startActivity(i2);
            }
        });

        calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(MainActivity.this, CalendarioManager.class);
                startActivity(i2);
            }
        });

        virtualroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i2 = new Intent(MainActivity.this, NavegadorSda.class);
                startActivity(i2);*/
                listView.setVisibility(View.VISIBLE);
                Lista();
                /*Alumno alumno = new Alumno();
                String elegido = alumno.getAlumnoElegido(getApplicationContext());
                String ruta = "http://sdavirtualroom.dyndns.org/sda/ingresoApp.php?mail="+elegido;

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(ruta));
                startActivity(intent);*/
            }
        });

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alumno alumno = new Alumno();
                alumno.setPPFFDni("",getApplicationContext());
                alumno.setPPFF("",getApplicationContext());
                alumno.setAlumnoElegido("",getApplicationContext());
                alumno.setAlumnosNombre("",getApplicationContext());
                alumno.setAlumnos("",getApplicationContext());

                Intent i = new Intent(MainActivity.this,InicioSesion.class);
                startActivity(i);
            }
        });

        String path = getApplicationContext().getFilesDir().getAbsolutePath() + "/conexion_url.txt";
        Log.e("aconformidad",""+path);
        File file = new File(path);
        file.exists();

        if(!file.exists()){
            Log.e("inconformidad",""+file.exists());

            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            @SuppressLint("MissingPermission") NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                Intent i = new Intent(MainActivity.this, InicioSesion.class);
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(),"Imposible conectarse al servidor... verifique su conexión a internet",Toast.LENGTH_LONG).show();
            }

        } else {
            Log.e("conformidad",""+file.exists());

            txt = (TextView) findViewById(R.id.usuario);
            Alumno alumno = new Alumno();
            String ppff[] = alumno.getPPFF(getApplicationContext()).split("&");
            txt.setText(ppff[0]);

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    100);
        }

    }

    private void Lista(){

        final Alumno alumno = new Alumno();
        final String[] valores = alumno.getAlumnosNombre(getApplicationContext()).split("_");
        final String[] valores2 = alumno.getAlumnos(getApplicationContext()).split("_");

        final ArrayList<Alumnos> arrayList = new ArrayList<Alumnos>();

        int n = valores.length;
        int i;
        for(i=0; i<n; i++){
            Alumnos alumnos = new Alumnos(valores2[i],valores[i]);
            arrayList.add(alumnos);

        }

        AdapterAlumnos adapterAlumnos = new AdapterAlumnos(this,arrayList);
        listView.setAdapter(adapterAlumnos);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listView.setVisibility(View.GONE);
            }
        });

    }

}