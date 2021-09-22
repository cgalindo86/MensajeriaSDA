package com.ingenio.mensajeriasda;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.ingenio.mensajeriasda.controler.DetalleMensaje;
import com.ingenio.mensajeriasda.controler.MensajeManager;
import com.ingenio.mensajeriasda.model.Alumno;
import com.ingenio.mensajeriasda.model.MensajeModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by cgali on 30/01/2018.
 */


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class BackgroundJobService extends JobService {


    private static final String CHANNEL_ID = "com.ingenio.instantmessagingsda";
    int notificationId;
    private Handler handler;
    Context context;
    com.github.nkzawa.socketio.client.Socket socket;
    float time;

    @Override
    public boolean onStartJob(final JobParameters params) {
        //Log.d(this.getClass().getSimpleName(),"onStartJob--");
        Log.e("BackgroundJobService 1","onStartJob--");
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        Log.e("formatedDate",formattedDate);

        createNotificationChannel();

        context = getApplicationContext();

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

                        mialumno.setAlumnoElegido(alumno,getApplicationContext());

                        MensajeModel mensajeModel = new MensajeModel();
                        mensajeModel.setMensajeElegido(mensajeria,getApplicationContext());
                        String mensajeria2 = mensajeModel.getMensajeNotificacion(getApplicationContext());

                        //Log.e("mensajeria",mensajeria);
                        //Log.e("mensajeria2",mensajeria2);

                        if(mial.contains(alumno)
                                && id_ppff.equals(mialumno.getPPFFDni(getApplicationContext()))
                                && !mensajeria2.equals(mensajeria)){
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
                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(BackgroundJobService.this);
                            Intent intent = new Intent(BackgroundJobService.this, DetalleMensaje.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            PendingIntent pendingIntent = PendingIntent.getActivity(BackgroundJobService.this, 0, intent, 0);

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(BackgroundJobService.this, CHANNEL_ID)
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

                        mialumno.setAlumnoElegido(alumno,getApplicationContext());

                        MensajeModel mensajeModel = new MensajeModel();
                        mensajeModel.setMensajeElegido(mensajeria,getApplicationContext());
                        String mensajeria2 = mensajeModel.getMensajeNotificacion(getApplicationContext());

                        //Log.e("mensajeria",mensajeria);
                        //Log.e("mensajeria2",mensajeria2);

                        if(mial.contains(alumno)
                                && id_ppff.equals(mialumno.getPPFFDni(getApplicationContext()))
                                && !mensajeria2.equals(mensajeria)){
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
                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(BackgroundJobService.this);
                            Intent intent = new Intent(BackgroundJobService.this, DetalleMensaje.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            PendingIntent pendingIntent = PendingIntent.getActivity(BackgroundJobService.this, 0, intent, 0);

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(BackgroundJobService.this, CHANNEL_ID)
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

        Log.e("BackgroundJobService 11","onStartJob--");
        BootReceiver.scheduleJob(getApplicationContext());
        return true;
    }


    /*
    * */
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




    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

}
