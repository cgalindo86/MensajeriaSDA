package com.ingenio.mensajeriasda;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.Calendar;

/**
 * Created by cgali on 30/01/2018.
 */

public class BootReceiver extends BroadcastReceiver {

    private static final int PERIOD_MS = 60000;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    int requestId=1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "procesos mensajeria", Toast.LENGTH_SHORT).show();

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Intent newIntent = new Intent(context, BackgroundIntentService.class);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, newIntent,
                    0);
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            manager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), PERIOD_MS,
                    pendingIntent);
        } else {
            scheduleJob(context);
        }
        Log.e("BootReceiver","boot receiver");

        /*Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(System.currentTimeMillis());
        calendar2.set(Calendar.HOUR_OF_DAY,7);
        calendar2.set(Calendar.MINUTE,58);
        calendar2.set(Calendar.SECOND,0);*/

        //setAlarm(1,calendar2.getTimeInMillis(),context);

        /*Calendar calendar3 = Calendar.getInstance();
        calendar3.setTimeInMillis(System.currentTimeMillis());
        calendar3.set(Calendar.HOUR_OF_DAY,11);
        calendar3.set(Calendar.MINUTE,17);
        calendar3.set(Calendar.SECOND, 0);*/

        //setAlarm2(1,calendar3.getTimeInMillis(),context);


    }



    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, BackgroundJobService.class);
        JobInfo.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new JobInfo.Builder(0, serviceComponent);
            //builder.setPeriodic(PERIOD_MS);
            builder.setMinimumLatency(PERIOD_MS);
            builder.setOverrideDeadline(PERIOD_MS);
            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(builder.build());
        }

    }

    public static void setAlarm(int i, Long timestamp, Context ctx) {
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(ctx, AlarmReceiver.class);
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast(ctx, i, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
        alarmIntent.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        //alarmManager.set(AlarmManager.RTC, timestamp,pendingIntent);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 60*1000, pendingIntent);
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
