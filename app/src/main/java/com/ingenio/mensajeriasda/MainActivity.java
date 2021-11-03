package com.ingenio.mensajeriasda;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
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
import com.ingenio.mensajeriasda.controler.CalificacionesManager;
import com.ingenio.mensajeriasda.controler.Mensaje;
import com.ingenio.mensajeriasda.controler.MensajeManager;
import com.ingenio.mensajeriasda.controler.PagosManager;
import com.ingenio.mensajeriasda.model.Alumno;
import com.ingenio.mensajeriasda.model.Eventos;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "com.ingenio.mensajeriasda";
    TextView txt;
    Button mensajes,calendario,calificaciones,pagos,virtualroom,cerrar,atencion;
    ListView listView;

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toast.makeText(getApplicationContext(),isConnected+"01",Toast.LENGTH_LONG).show();
        //Log.i("inicio","inicio");

        Intent i2 = new Intent(MainActivity.this,MainActivity.class);
        BootReceiver bootReceiver = new BootReceiver();
        bootReceiver.onReceive(getApplicationContext(),i2);

        atencion = (Button) findViewById(R.id.atencion);
        mensajes = (Button) findViewById(R.id.mensajes);
        calendario = (Button) findViewById(R.id.calendario);
        calificaciones = (Button) findViewById(R.id.calificaciones);
        pagos = (Button) findViewById(R.id.pagos);
        virtualroom = (Button) findViewById(R.id.virtualroom);
        cerrar = (Button) findViewById(R.id.cerrar);
        listView = (ListView) findViewById(R.id.milista);
        listView.setVisibility(View.GONE);

        atencion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alumno alumno = new Alumno();
                String nalumno = alumno.getAlumnoElegido(getApplicationContext());
                String nombre[] = alumno.getAlumnoData(nalumno,getApplicationContext()).split("&");

                String mensajeria="Estimado personal dominguino"
                        +". Soy apoderado de "+nombre[1]
                        +". Tengo la siguiente consulta:\n";

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"atencionalcliente@sda.edu.pe"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Consulta");
                intent.putExtra(Intent.EXTRA_TEXT, mensajeria);
                startActivity(Intent.createChooser(intent, "Consulta"));
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

        virtualroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listView.setVisibility(View.VISIBLE);
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

            /*Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY,18);
            calendar.set(Calendar.MINUTE,59);
            calendar.set(Calendar.SECOND, 0);

            //Utils utils = new Utils();
            setAlarm(1,calendar.getTimeInMillis(),getApplicationContext());



            calendar3.set(Calendar.HOUR_OF_DAY,16);
            calendar3.set(Calendar.MINUTE,48);
            calendar3.set(Calendar.SECOND, 0);

            setAlarm2(1,calendar3.getTimeInMillis(),getApplicationContext());*/
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