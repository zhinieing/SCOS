package es.source.code.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import es.source.code.activity.FoodDetailed;
import es.source.code.activity.MyApplication;
import es.source.code.activity.R;
import es.source.code.model.Food;

public class UpdateService extends IntentService {

    private static final String CHANNEL_ID = "es.source.code.service";
    private static final String CHANNEL_NAME = "DEFAULT CHANNEL";

    //private ArrayList<Food> foods = null;


    public UpdateService() {
        super("UpdateService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        /*SharedPreferences sharedPreferences = getSharedPreferences("userdata", MODE_PRIVATE);
        Long lastTime = sharedPreferences.getLong("lastTime", 0);
        Date lastDate = new Date(lastTime);


        BmobQuery<Food> syncQuery = new BmobQuery<Food>();
        syncQuery.addWhereGreaterThan("createdAt", new BmobDate(lastDate));
        syncQuery.order("-createdAt");
        syncQuery.setLimit(1);
        syncQuery.findObjects(new FindListener<Food>() {
            @Override
            public void done(List<Food> list, BmobException e) {
                if(e == null){

                    for(Food food : list){
                        foods.add(food);
                    }
                }
            }
        });*/

        Food food = new Food("凉拌海带丝", 18, "", "",
                "冷菜", 20, 0, false);


        if(food != null){
            Intent detailIntent = new Intent(UpdateService.this, FoodDetailed.class);
            PendingIntent pi = PendingIntent.getActivity(UpdateService.this, 0, detailIntent, 0);


            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder notificationCompatBuilder;

            if(Build.VERSION.SDK_INT < 26){
                notificationCompatBuilder = new NotificationCompat.Builder(UpdateService.this);
            } else {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                channel.enableLights(true);
                channel.enableVibration(true);
                channel.setLightColor(Color.GREEN);
                channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                manager.createNotificationChannel(channel);
                notificationCompatBuilder = new NotificationCompat.Builder(UpdateService.this, CHANNEL_ID);
            }


            Notification notification = notificationCompatBuilder
                    .setContentTitle("新品上架")
                    .setContentText("菜名:" + food.getFoodName() + " " + "价格:" + "¥" + food.getFoodPrice() + " " + "类型:" + food.getKind())
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.socs_launcher))
                    .setContentIntent(pi)
                    .build();

            manager.notify(1, notification);
        }

    }

}
