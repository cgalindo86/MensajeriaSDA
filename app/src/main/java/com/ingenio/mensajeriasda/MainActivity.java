package com.ingenio.mensajeriasda;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
//import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.ingenio.mensajeriasda.controler.AdapterAlumnos;
import com.ingenio.mensajeriasda.model.Alumnos;
import com.ingenio.mensajeriasda.controler.CalendarioManager;
import com.ingenio.mensajeriasda.controler.CalificacionesManager;
import com.ingenio.mensajeriasda.controler.CanalesAtencion;
import com.ingenio.mensajeriasda.controler.MapsActivity;
import com.ingenio.mensajeriasda.controler.MensajeManager;
import com.ingenio.mensajeriasda.controler.MiQr;
import com.ingenio.mensajeriasda.controler.PagosManager;
import com.ingenio.mensajeriasda.model.Alumno;
import com.ingenio.mensajeriasda.model.Eventos;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "com.ingenio.mensajeriasda";
    TextView txt,txt2;
    Button mensajes,calendario,calificaciones,pagos,virtualroom,cerrar,atencion,
            declaracion,carnet,fichaCovid,fichaMedica,movilidad,cerrar2;
    ListView listView;
    LinearLayout linearLayout,linearLayout2;
    int ancho,alto,medida,medida2;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toast.makeText(getApplicationContext(),isConnected+"01",Toast.LENGTH_LONG).show();
        //Log.i("inicio","inicio");


        Intent i2 = new Intent(MainActivity.this,MainActivity.class);
        BootReceiver bootReceiver = new BootReceiver();
        bootReceiver.onReceive(getApplicationContext(),i2);

        txt2 = (TextView) findViewById(R.id.textoSeleccionado);
        atencion = (Button) findViewById(R.id.atencion);
        mensajes = (Button) findViewById(R.id.mensajes);
        calendario = (Button) findViewById(R.id.calendario);
        calificaciones = (Button) findViewById(R.id.calificaciones);
        pagos = (Button) findViewById(R.id.pagos);
        virtualroom = (Button) findViewById(R.id.virtualroom);
        cerrar = (Button) findViewById(R.id.cerrar);
        cerrar2 = (Button) findViewById(R.id.cerrar2);
        carnet = (Button) findViewById(R.id.carnet);
        //declaracion = (Button) findViewById(R.id.declaracion);
        fichaCovid = (Button) findViewById(R.id.fichaCovid);
        fichaMedica = (Button) findViewById(R.id.fichaMedica);
        movilidad = (Button) findViewById(R.id.movilidad);
        listView = (ListView) findViewById(R.id.milista);
        linearLayout = (LinearLayout) findViewById(R.id.lalista);
        linearLayout2 = (LinearLayout) findViewById(R.id.lalista2);
        linearLayout.setVisibility(View.GONE);

        Display display = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        ancho = point.x; alto = point.y;
        if(ancho>alto){
            medida = ancho/2;
            medida2 = ancho-40;
        } else {
            medida = ancho/2;
            medida2 = ancho-40;
        }
        ViewGroup.LayoutParams params0 = linearLayout2.getLayoutParams();
        params0.width = medida2;
        linearLayout2.setLayoutParams(params0);
        ViewGroup.LayoutParams params1 = atencion.getLayoutParams();
        params1.width = medida;
        atencion.setLayoutParams(params1);
        ViewGroup.LayoutParams params2 = calendario.getLayoutParams();
        params2.width = medida;
        calendario.setLayoutParams(params2);
        ViewGroup.LayoutParams params3 = calificaciones.getLayoutParams();
        params3.width = medida;
        calificaciones.setLayoutParams(params3);
        ViewGroup.LayoutParams params4 = mensajes.getLayoutParams();
        params4.width = medida;
        mensajes.setLayoutParams(params4);
        ViewGroup.LayoutParams params5 = pagos.getLayoutParams();
        params5.width = medida;
        pagos.setLayoutParams(params5);
        ViewGroup.LayoutParams params6 = virtualroom.getLayoutParams();
        params6.width = medida;
        virtualroom.setLayoutParams(params5);
        //declaracion.setLayoutParams(params5);
        carnet.setLayoutParams(params5);
        fichaMedica.setLayoutParams(params5);
        fichaCovid.setLayoutParams(params5);
        movilidad.setLayoutParams(params5);
        //cerrar.setLayoutParams(params5);

        atencion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CanalesAtencion.class);
                startActivity(i);

            }
        });

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

        calificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(MainActivity.this, CalificacionesManager.class);
                startActivity(i2);
            }
        });

        pagos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(MainActivity.this, PagosManager.class);
                startActivity(i2);
            }
        });

        carnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(MainActivity.this, MiQr.class);
                startActivity(i2);
            }
        });

        virtualroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alumno alumno = new Alumno();
                alumno.setAlumnoOpcion("virtual",getApplicationContext());
                txt2.setText("ACCESO AL VIRTUAL ROOM");
                linearLayout.setVisibility(View.VISIBLE);
                Lista();

            }
        });

        fichaMedica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alumno alumno = new Alumno();
                alumno.setAlumnoOpcion("declaracion",getApplicationContext());
                txt2.setText("ACCESO A LA FICHA MÉDICA");
                linearLayout.setVisibility(View.VISIBLE);
                Lista();

            }
        });

        fichaCovid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alumno alumno = new Alumno();
                alumno.setAlumnoOpcion("covid",getApplicationContext());
                txt2.setText("ACCESO A LA FICHA COVID");
                linearLayout.setVisibility(View.VISIBLE);
                Lista();
            }
        });

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Eventos eventos = new Eventos();

                Alumno alumno = new Alumno();
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

                alumno.setPPFFDni("",getApplicationContext());
                alumno.setPPFF("",getApplicationContext());
                alumno.setAlumnoElegido("",getApplicationContext());
                alumno.setAlumnosNombre("",getApplicationContext());
                alumno.setAlumnos("",getApplicationContext());

                Intent i = new Intent(MainActivity.this,InicioSesion.class);
                startActivity(i);
            }
        });

        cerrar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.setVisibility(View.GONE);
            }
        });

        movilidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
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
            Log.e("roles2",alumno.getAlumnoPPFFRol(getApplicationContext()));
            if(ppff[3].equals("PPFF")){
                Limpiar1();
            } else {
                Limpiar2();
            }

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.LOCATION_HARDWARE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    100);

        }

    }

    void Limpiar1(){
        atencion.setVisibility(View.VISIBLE);
        mensajes.setVisibility(View.VISIBLE);
        calendario.setVisibility(View.VISIBLE);
        calificaciones.setVisibility(View.VISIBLE);
        pagos.setVisibility(View.VISIBLE);
        virtualroom.setVisibility(View.VISIBLE);
        cerrar.setVisibility(View.VISIBLE);
        carnet.setVisibility(View.VISIBLE);
        fichaCovid.setVisibility(View.VISIBLE);
        fichaMedica.setVisibility(View.VISIBLE);
        movilidad.setVisibility(View.VISIBLE);

    }

    void Limpiar2(){
        atencion.setVisibility(View.VISIBLE);
        mensajes.setVisibility(View.GONE);
        calendario.setVisibility(View.GONE);
        calificaciones.setVisibility(View.GONE);
        pagos.setVisibility(View.GONE);
        virtualroom.setVisibility(View.GONE);
        cerrar.setVisibility(View.VISIBLE);
        carnet.setVisibility(View.VISIBLE);
        fichaCovid.setVisibility(View.GONE);
        fichaMedica.setVisibility(View.GONE);
        movilidad.setVisibility(View.VISIBLE);
    }

    private void Lista(){
        final ListView lista = (ListView) findViewById(R.id.milista);
        final Alumno alumno = new Alumno();
        final String[] valores = alumno.getAlumnosNombre(getApplicationContext()).split("_");
        final String[] valores2 = alumno.getAlumnos(getApplicationContext()).split("_");

        final ArrayList<Alumnos> arrayList = new ArrayList<Alumnos>();

        int n = valores.length;
        int i;
        for(i=0; i<n; i++){
            Log.e("n",n+"");
            Log.e("valores",valores[i]);
            Log.e("valores2",valores2[i]);
            Alumnos alumnos = new Alumnos(valores2[i],valores[i]);
            arrayList.add(alumnos);

        }

        AdapterAlumnos adapterAlumnos = new AdapterAlumnos(this,arrayList);
        lista.setAdapter(adapterAlumnos);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                linearLayout.setVisibility(View.GONE);
            }
        });

    }

    public static void setAlarm(int i, Long timestamp, Context ctx) {
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(ctx, AlarmReceiver.class);
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast(ctx, i, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
        alarmIntent.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timestamp, 60*1000,pendingIntent);
        //alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 60*1000, pendingIntent);
    }

    public static void setAlarm2(int i, Long timestamp, Context ctx) {
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(ctx, AlarmReceiver.class);
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast(ctx, i, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
        alarmIntent.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        alarmManager.setRepeating(AlarmManager.RTC, timestamp, 60*1000,pendingIntent);
        //alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 60*1000, pendingIntent);
    }

}