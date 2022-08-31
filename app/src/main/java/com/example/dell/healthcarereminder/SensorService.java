package com.example.dell.healthcarereminder;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

class Hcr
{
    String name;
    String type;

}

public class SensorService extends Service {
    DatabaseHelper db1 = new DatabaseHelper(this);
    public int counter=0;
    public SensorService(Context applicationContext) {
        super();
        Log.i("HERE", "here I am!");
    }

    public SensorService() {
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent("uk.ac.shef.oak.ActivityRecognition.RestartSensor");
        sendBroadcast(broadcastIntent);
        stoptimertask();
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime=0;
    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 0, 1000); //
    }


    public void initializeTimerTask()
    {
        final SensorService st=this;
        timerTask = new TimerTask()
        {
            public void run()
            {

                st.check();
            }
        };
    }

    public void check()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date1 = new Date();
        String date="";
        int time=0;
        System.out.println(dateFormat.format(date1));
        String temp = dateFormat.format(date1);
        int i=0;
        for(i=0;temp.charAt(i)!=' ';i++)
        {
            if(temp.charAt(i)=='/')
            {
                date=date+"-";
            }
            else
                date=date+temp.charAt(i);
        }
        i++;
        for(;temp.charAt(i)!=':';i++)
        {
            time=(time*10)+(temp.charAt(i)-'0');
        }
        time=time*60*60;
        int time1=0;
        i++;
        for(;temp.charAt(i)!=':';i++)
        {
            time1=(time1*10)+(temp.charAt(i)-'0');
        }
        time=time+(time1*60);
        Log.i("ALARM","dATE"+(date)+"Time"+(time));

        String selectQuery = "SELECT * FROM " + "hcr_table" + " WHERE date= '"+ date +"' AND time= " + time + " ORDER BY " + "date , time" + " DESC " ;
        Log.i("ALARM",selectQuery);
        SQLiteDatabase db = db1.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int id;
        String name,type;
        if (cursor.moveToFirst())
        {
            do
            {

                type= cursor.getString(1);
                String text="";
                id = cursor.getInt(2);
                int id1 = cursor.getInt(0);
                Log.i("ALARMIN","type"+(type));
                Hcr h = new Hcr();
                if(type.equals("general_table"))
                {
                    text = "General Reminder";
                }
                if(type.equals("water_table"))
                {
                    text = "Water Reminder";
                }
                if(type.equals("exercise_table"))
                {
                    text = "Exercise Reminder";
                }
                if(type.equals("medicine_table"))
                {
                    text = "Medicine Reminder";
                }
                h.type=text;
                h.name=db1.getname(type,id);
                Log.i("ALARMIN","name"+(db1.getname(type,id)));
                showNotification(h,counter);
                db1.delete(type,id,id1);   //////////////////////////////////
                //notify1(h,counter);
                counter++;

            } while (cursor.moveToNext());
        }

    }

    public void showNotification(Hcr node ,  int i)
    {
        String id = "Main_Activity";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            CharSequence name = "Health care Reminder";
            String description = node.type;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(id,name,importance);
            notificationChannel.setDescription(description);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.WHITE);
            notificationChannel.enableVibration(false);
            if(nm != null)
            {
                nm.createNotificationChannel(notificationChannel);
            }

        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,id);
        notificationBuilder.setSmallIcon(R.drawable.icon);
        notificationBuilder.setContentTitle(node.type);
        notificationBuilder.setContentText(node.name);
        notificationBuilder.setLights(Color.WHITE,500,5000);
        notificationBuilder.setColor(Color.RED);
        notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(i,notificationBuilder.build());

    }

    public void notify1(Hcr node,int i)
    {
        Log.i("in notification","notify");
        Intent intent = new Intent();
        PendingIntent pIntent = PendingIntent.getActivity(SensorService.this,0,intent,0);
        Notification noti = new Notification.Builder(SensorService.this)
                .setTicker("Reminder")
                .setContentTitle(node.type)
                .setContentText(node.name)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pIntent)
                .build();

        noti.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(i , noti);
        Log.i("out notification","notify");
    }

    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}