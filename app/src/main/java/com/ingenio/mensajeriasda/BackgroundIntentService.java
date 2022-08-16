package com.ingenio.mensajeriasda;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;

import com.ingenio.mensajeriasda.controler.CalendarioManager;
import com.ingenio.mensajeriasda.controler.DetalleMensaje;
import com.ingenio.mensajeriasda.model.Alumno;
import com.ingenio.mensajeriasda.model.MensajeModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by cgali on 30/01/2018.
 */

public class BackgroundIntentService extends IntentService {

    private static final String CHANNEL_ID = "com.ingenio.instantmessagingsda";
    int notificationId;
    private Handler handler;
    Context context;
    com.github.nkzawa.socketio.client.Socket socket;
    float time;
    String[] fechas= new String[]{
            "2022-02-24","2022-02-25","2022-02-26","2022-02-27",
            "2022-02-28","2022-03-01","2022-03-05","2022-03-06",
            "2022-03-12","2022-03-13","2022-03-19","2022-03-20",
            "2022-03-26","2022-03-27","2022-04-02","2022-04-02",
            "2022-04-09","2022-04-10","2022-04-14","2022-04-15",
            "2022-04-16","2022-04-17","2022-04-23","2022-04-24",
            "2022-04-30","2022-05-01","2022-05-07","2022-05-08",
            "2022-05-09","2022-05-10","2022-05-11","2022-05-12",
            "2022-05-13","2022-05-14","2022-05-15","2022-05-21",
            "2022-05-22","2022-05-28","2022-05-29","2022-06-04",
            "2022-06-05","2022-06-11","2022-06-12","2022-05-18",
            "2022-06-19","2022-06-25","2022-06-26","2022-07-02",
            "2022-07-03","2022-07-09","2022-07-10","2022-07-16",
            "2022-07-17","2022-07-23","2022-07-24","2022-07-25",
            "2022-07-26","2022-07-27","2022-07-28","2022-07-29",
            "2022-07-30","2022-07-31","2022-08-06","2022-08-07",
            "2022-08-13","2022-08-14","2022-08-20","2022-08-21",
            "2022-08-27","2022-08-28","2022-08-30","2022-09-03",
            "2022-09-04","2022-09-10","2022-09-11","2022-09-17",
            "2022-09-18","2022-09-24","2022-09-25","2022-10-01",
            "2022-10-02","2022-10-03","2022-10-04","2022-10-05",
            "2022-10-06","2022-10-07","2022-10-08","2022-10-09",
            "2022-10-15","2022-10-16","2022-10-22","2022-10-23",
            "2022-10-29","2022-10-30","2022-11-01","2022-11-05",
            "2022-11-06","2022-11-12","2022-11-13","2022-11-19",
            "2022-11-20","2022-11-26","2022-11-27","2022-12-03",
            "2022-12-04","2022-12-08","2022-12-10","2022-12-11",
            "2022-12-17","2022-12-18","2022-12-19","2022-12-20",
            "2022-12-21","2022-12-22","2022-12-23","2022-12-24",
            "2022-12-25","2022-12-26","2022-12-27","2022-12-28",
            "2022-12-29","2022-12-30","2022-12-31"
    };

    String[] horas= new String[]{
            "I3_06:31","I4_06:35","I5_06:40","P1_06:45",
            "P2_06:50","P3_06:55","P4_07:01","P5_07:05",
            "P6_07:10","S1_07:15","S2_07:20","S3_07:25",
            "S4_07:30","S5_07:35"
    };

    String[] tareas = new String[]{};

    String ruta="";

    /**
     * @param name
     * @deprecated
     */
    public BackgroundIntentService(String name) {
        super(name);
    }

    @SuppressLint("WrongThread")
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //Log.d(this.getClass().getSimpleName(),"onStartJob--");
        Log.e("BackgroundJobService 1","onStartJob--");
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        Log.e("formatedDate",formattedDate);

        SimpleDateFormat df2 = new SimpleDateFormat("HH:mm");
        String formattedDate2 = df2.format(c.getTime());
        Log.e("formatedDate",formattedDate2);

        Log.e("eee",!Comparar(formattedDate,fechas)+"");


        createNotificationChannel();

        context = getApplicationContext();

        Alumno alumno = new Alumno();
        Log.e("ver",alumno.getAlumnos(getApplicationContext()));
        String als[] = alumno.getAlumnos(getApplicationContext()).split("_");

        int i;
        for(i=0; i<=als.length-1; i++){
            String dni = als[i];
            String dalumno1[] = alumno.getAlumnoData(dni,getApplicationContext()).split("&");
            if(dalumno1.length>=2){
                String grado = dalumno1[2];

                if(Comparar2(grado+"_"+formattedDate2,horas)){

                    BuscarEventos(dni,formattedDate);

                }
            }
        }


        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        opts.reconnection = false;
        try {

            socket = IO.socket("http://sdavirtualroom.dyndns.org:8013");

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        socket.connect();

        Alumno mialumno = new Alumno();
        Log.e("ppff - dni",mialumno.getPPFFDni(getApplicationContext()));

        socket.on("recibe info2 "+mialumno.getPPFFDni(getApplicationContext()), new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                Handler nHandler = new Handler(getMainLooper());
                nHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(),"recibe info 01",Toast.LENGTH_LONG).show();
                        JSONObject data = (JSONObject) args[0];
                        String supervisor,supervisorMail,supervisorCelular,
                                id_ppff,alumno,alumnoNombre,fecha,asunto,curso,mensaje,hora;
                        try {
                            supervisor = data.getString("supervisor");
                            supervisorMail = data.getString("supervisorMail");
                            supervisorCelular = data.getString("supervisorCelular");
                            id_ppff = data.getString("id_ppff");
                            alumno = data.getString("alumno");
                            alumnoNombre = data.getString("alumnoNombre");
                            fecha = data.getString("fecha");
                            hora = data.getString("hora");
                            asunto = data.getString("asunto");
                            curso = data.getString("curso");
                            //mensaje = data.getString("mensaje");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }

                        Log.d("MainActivity",supervisor);
                        Log.d("MainActivity",supervisorMail);
                        Log.d("MainActivity",supervisorCelular);
                        Log.d("MainActivity",id_ppff);
                        Log.e("alumno",alumno);
                        Log.d("MainActivity",alumnoNombre);
                        Log.d("MainActivity",fecha);
                        Log.d("MainActivity",asunto);
                        Log.d("MainActivity",curso);

                        Alumno mialumno = new Alumno();
                        String mial = mialumno.getAlumnos(getApplicationContext());
                        String mensajeria = supervisor+"%"+supervisorMail+"%"+supervisorCelular+"%"+
                                id_ppff+"%"+alumno+"%"+alumnoNombre+"%"+fecha+"%"+hora+"%"+asunto+"%"+curso+"%#";

                        //mialumno.setAlumnoElegido(alumno,getApplicationContext());

                        MensajeModel mensajeModel = new MensajeModel();
                        mensajeModel.setMensajeElegido(mensajeria,getApplicationContext());
                        String mensajeria2 = mensajeModel.getMensajeNotificacion(getApplicationContext());

                        //Log.e("mensajeria",mensajeria);
                        //Log.e("mensajeria2",mensajeria2);

                        if(mial.contains(alumno)
                                && id_ppff.equals(mialumno.getPPFFDni(getApplicationContext()))
                                && !mensajeria2.equals(mensajeria)
                                && !Comparar(formattedDate,fechas)
                        ){
                            Log.e("contenido",mial);
                            mensajeria2 = mensajeria;
                            //String mensajeria = supervisor+"%"+supervisorMail+"%"+supervisorCelular+"%"+
                            //        id_ppff+"%"+alumno+"%"+alumnoNombre+"%"+fecha+"%"+hora+"%"+asunto+"%"+curso+"%#";
                            //MensajeModel mensajeModel = new MensajeModel();
                            String eldato = mensajeModel.getMensaje(getApplicationContext());
                            mensajeria = mensajeria + eldato;
                            mensajeModel.setMensaje(mensajeria,getApplicationContext());
                            mensajeModel.setMensajeNotificacion(mensajeria2,getApplicationContext());

                            int icono = R.drawable.icono_blanco;

                            notificationId++;
                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(BackgroundIntentService.this);
                            Intent intent = new Intent(BackgroundIntentService.this, DetalleMensaje.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            PendingIntent pendingIntent = PendingIntent.getActivity(BackgroundIntentService.this, 0, intent, 0);

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(BackgroundIntentService.this, CHANNEL_ID)
                                    .setSmallIcon(icono)
                                    .setContentTitle("SDA Instant Messaging de "+alumnoNombre)
                                    .setContentText(asunto)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText(asunto))
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setContentIntent(pendingIntent)
                                    .setAutoCancel(true);
                            notificationManager.notify(notificationId, builder.build());
                        } else {
                            Log.e("no contenido",mial);
                        }

                    }
                });
            }
        });

        socket.on("recibe info3 "+mialumno.getPPFFDni(getApplicationContext()), new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                Handler nHandler = new Handler(getMainLooper());
                nHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(),"recibe info 01",Toast.LENGTH_LONG).show();
                        JSONObject data = (JSONObject) args[0];
                        String supervisor,supervisorMail,supervisorCelular,
                                id_ppff,alumno,alumnoNombre,fecha,asunto,curso,mensaje,hora;
                        try {
                            supervisor = data.getString("supervisor");
                            supervisorMail = data.getString("supervisorMail");
                            supervisorCelular = data.getString("supervisorCelular");
                            id_ppff = data.getString("id_ppff");
                            alumno = data.getString("alumno");
                            alumnoNombre = data.getString("alumnoNombre");
                            fecha = data.getString("fecha");
                            hora = data.getString("hora");
                            asunto = data.getString("asunto");
                            curso = data.getString("curso");
                            //mensaje = data.getString("mensaje");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }

                        Log.d("MainActivity",supervisor);
                        Log.d("MainActivity",supervisorMail);
                        Log.d("MainActivity",supervisorCelular);
                        Log.d("MainActivity",id_ppff);
                        Log.e("alumno",alumno);
                        Log.d("MainActivity",alumnoNombre);
                        Log.d("MainActivity",fecha);
                        Log.d("MainActivity",asunto);
                        Log.d("MainActivity",curso);

                        Alumno mialumno = new Alumno();
                        String mial = mialumno.getAlumnos(getApplicationContext());
                        String mensajeria = supervisor+"%"+supervisorMail+"%"+supervisorCelular+"%"+
                                id_ppff+"%"+alumno+"%"+alumnoNombre+"%"+fecha+"%"+hora+"%"+asunto+"%"+curso+"%#";

                        //mialumno.setAlumnoElegido(alumno,getApplicationContext());

                        MensajeModel mensajeModel = new MensajeModel();
                        mensajeModel.setMensajeElegido(mensajeria,getApplicationContext());
                        String mensajeria2 = mensajeModel.getMensajeNotificacion(getApplicationContext());

                        //Log.e("mensajeria",mensajeria);
                        //Log.e("mensajeria2",mensajeria2);

                        if(mial.contains(alumno)
                                && id_ppff.equals(mialumno.getPPFFDni(getApplicationContext()))
                                && !mensajeria2.equals(mensajeria)
                                && !Comparar(formattedDate,fechas)
                        ){
                            Log.e("contenido",mial);
                            mensajeria2 = mensajeria;
                            //String mensajeria = supervisor+"%"+supervisorMail+"%"+supervisorCelular+"%"+
                            //        id_ppff+"%"+alumno+"%"+alumnoNombre+"%"+fecha+"%"+hora+"%"+asunto+"%"+curso+"%#";
                            //MensajeModel mensajeModel = new MensajeModel();
                            String eldato = mensajeModel.getMensaje(getApplicationContext());
                            mensajeria = mensajeria + eldato;
                            mensajeModel.setMensaje(mensajeria,getApplicationContext());
                            mensajeModel.setMensajeNotificacion(mensajeria2,getApplicationContext());

                            int icono = R.drawable.icono_blanco;

                            notificationId++;
                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(BackgroundIntentService.this);
                            Intent intent = new Intent(BackgroundIntentService.this, DetalleMensaje.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            PendingIntent pendingIntent = PendingIntent.getActivity(BackgroundIntentService.this, 0, intent, 0);

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(BackgroundIntentService.this, CHANNEL_ID)
                                    .setSmallIcon(icono)
                                    .setContentTitle("SDA Instant Messaging de "+alumnoNombre)
                                    .setContentText(asunto)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText(asunto))
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setContentIntent(pendingIntent)
                                    .setAutoCancel(true);
                            notificationManager.notify(notificationId, builder.build());
                        } else {
                            Log.e("no contenido",mial);
                        }

                    }
                });
            }
        });

        Log.e("boot","onStartJob--");

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void BuscarEventos(String dalumno,String dia){
        Alumno alumno = new Alumno();
        String dalumno1[] = alumno.getAlumnoData(dalumno,getApplicationContext()).split("&");
        String nombre = dalumno1[1];
        String grado = dalumno1[2];
        String seccion = dalumno1[3];
        String nivel="";
        if(grado.contains("I")){
            nivel = "I";
        } else if(grado.contains("P")){
            nivel = "P";
        } else {
            nivel = "S";
        }

        String[] dd = dia.split("-");

        ruta = "http://sdavirtualroom.dyndns.org/sda/controler/consultaAlumno.php?accionget=5&alumnoget="+dalumno
                +"&gradoget="+grado+"&seccionget="+seccion+"&nivelget="+nivel+"&anioget="+dd[0]
                +"&mesget="+dd[1]+"&diaget="+dd[2];

        LeeDatos leeDatos = new LeeDatos();
        leeDatos.execute(ruta);
    }

    public class LeeDatos extends AsyncTask<String,Void,String> {

        ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {
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
            //Eventos eventos = new Eventos();
            //eventos.setDatosMes(result,mesActual,elegido,getApplicationContext());
            Log.e("resultadoLeeDatos", result);
            EventosEnCalendario(result);

        }

    }

    void EventosEnCalendario(String eventos){

        //String ev[] = {"12/10/2018 20:00:00","12/11/2018 20:00:00","12/12/2018 20:00:00","12/14/2018 20:00:00"};
        String valu[] = eventos.split("_##");
        //compactCalendarView.removeAllEvents();

        int val = valu.length;
        //Log.d("valu length", val+"");
        for (int i = 0; i <= val-1; i++) {
            //Log.e("ev",valu[i]);
            String matriz[] = valu[i].split("_");
            Log.e("evento",valu[i]);

            if(matriz[1].equals("1")){
                Notificaciones("Tarea de hoy - "+matriz[6],"Curso: "+matriz[2]+" - "+matriz[3]);
            } else if(matriz[1].equals("2")){
                Notificaciones("Materiales de hoy - "+matriz[6],"Curso: "+matriz[2]+" - "+matriz[3]);
            } else if(matriz[1].equals("3")){
                Notificaciones("Actividad de hoy - "+matriz[6],matriz[3]);
            } else if(matriz[1].equals("4")){
                Notificaciones("Evaluación de hoy - "+matriz[6],"Curso: "+matriz[2]+" - "+matriz[3]);
            } else if(matriz[1].equals("5")){
                Notificaciones("Actividad de hoy - "+matriz[6],matriz[3]);
            }

        }



    }

    private void Notificaciones(String data0, String data1){
        int icono = R.drawable.icono_blanco;

        notificationId++;
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(BackgroundIntentService.this);
        Intent intent = new Intent(BackgroundIntentService.this, CalendarioManager.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(BackgroundIntentService.this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(BackgroundIntentService.this, CHANNEL_ID)
                .setSmallIcon(icono)
                .setContentTitle(data0)
                .setContentText(data1)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(data1))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        notificationManager.notify(notificationId, builder.build());
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

    public boolean Comparar(String lafecha, String[] fechas){
        boolean valor= true;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            valor = Arrays.stream(fechas).anyMatch(lafecha::equals);
        }
        return valor;
    }

    public boolean Comparar2(String lafecha, String[] fechas){
        boolean valor= true;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            valor = Arrays.stream(fechas).anyMatch(lafecha::contains);
        }
        return valor;
    }



    public String getAlumno(String entrada) {
        URL alumUrl = null;
        //Class<java.net.URL> aClass = java.net.URL.class;
        String url2 = "";
        try {
            alumUrl = new URL(entrada);
            HttpURLConnection conn = (HttpURLConnection) alumUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            byte[] array = new byte[1000]; // buffer temporal de lectura.
            StringBuffer out = new StringBuffer();
            byte[] b = new byte[4096];
            for (int n; (n = is.read(b)) != -1; ) {
                out.append(new String(b, 0, n, "UTF-8"));
            }
            String pot = new String(out.toString().getBytes("UTF-8"));
            url2 = pot;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Log.d("Consulta", url2);
        return url2;
    }

    /*
     * */


}