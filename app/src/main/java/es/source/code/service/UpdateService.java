package es.source.code.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import es.source.code.activity.FoodDetailed;
import es.source.code.activity.R;

public class UpdateService extends IntentService {

    private static final String CHANNEL_ID = "es.source.code.service";
    private static final String CHANNEL_NAME = "DEFAULT CHANNEL";


    public UpdateService() {
        super("UpdateService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {



        Intent detailIntent = new Intent(this, FoodDetailed.class);

        PendingIntent pi = PendingIntent.getActivity(this, 0, detailIntent, 0);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationCompatBuilder;

        if(Build.VERSION.SDK_INT < 26){
            notificationCompatBuilder = new NotificationCompat.Builder(this);
        } else {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLightColor(Color.GREEN);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            manager.createNotificationChannel(channel);
            notificationCompatBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        }

        Notification notification = notificationCompatBuilder
                .setContentTitle("新品菜价")
                .setContentText("菜名"+"价格"+"类型")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.socs_launcher))
                .setContentIntent(pi)
                .build();

        manager.notify(1, notification);
    }


}
