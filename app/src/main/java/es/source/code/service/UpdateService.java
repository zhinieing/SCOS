package es.source.code.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.RemoteViews;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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
import es.source.code.activity.MainScreen;
import es.source.code.activity.MyApplication;
import es.source.code.activity.R;
import es.source.code.model.Food;

import static cn.bmob.v3.b.From.e;

public class UpdateService extends IntentService {

    private List<Food> foods;
    private MediaPlayer mediaPlayer;
    private Handler mHandler = new Handler();


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

        /*
        private static final String CHANNEL_ID = "es.source.code.service";
        private static final String CHANNEL_NAME = "DEFAULT CHANNEL";
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
            */



        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try{
            URL url = new URL("http://10.0.2.2:8080/SCOSServer/FoodUpdateService");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);

            InputStream in = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                response.append(line);
            }

            //foods = parseJSON(response.toString());
            foods = parseXML(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null){
                connection.disconnect();
            }
        }


        if(foods != null){
            Uri uri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE);
            mediaPlayer = MediaPlayer.create(this, uri);

            try {
                mediaPlayer.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.stop();
                }
            }, 3000);



            Intent detailIntent = new Intent(UpdateService.this, MainScreen.class);
            PendingIntent pi = PendingIntent.getActivity(UpdateService.this, 0, detailIntent, 0);


            RemoteViews views = new RemoteViews(getPackageName(), R.layout.notification);
            PendingIntent btn = PendingIntent.getBroadcast(this, 1, new Intent("es.source.code.service.notification"), 0);
            views.setOnClickPendingIntent(R.id.btn_update, btn);

            views.setTextViewText(R.id.tv_update, "新品上架：菜品数量" + foods.size());


            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            Notification notification = new NotificationCompat.Builder(UpdateService.this)
                    .setContent(views)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pi)
                    .build();

            manager.notify(1, notification);
        }

    }

    private List<Food> parseJSON(String data){
        Gson gson = new Gson();
        List<Food> foods = gson.fromJson(data, new TypeToken<List<Food>>(){}.getType());
        return foods;
    }

    private List<Food> parseXML(String data){
        List<Food> foods = new ArrayList<Food>();
        XmlPullParserFactory factory = null;

        try {
            factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(data));
            int eventType = xmlPullParser.getEventType();

            String foodName = "";
            int foodPrice = 0;
            String kind = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    // 开始解析某个节点
                    case XmlPullParser.START_TAG: {
                        if ("foodName".equals(nodeName)) {
                            foodName = xmlPullParser.nextText();
                        } else if ("foodPrice".equals(nodeName)) {
                            foodPrice = Integer.parseInt(xmlPullParser.nextText());
                        } else if ("kind".equals(nodeName)) {
                            kind = xmlPullParser.nextText();
                        }
                        break;
                    }
                    // 完成解析某个节点
                    case XmlPullParser.END_TAG: {
                        if ("food".equals(nodeName)){
                            Food food = new Food();
                            food.setFoodName(foodName);
                            food.setFoodPrice(foodPrice);
                            food.setKind(kind);
                            foods.add(food);
                        }
                        break;
                    }
                    default:
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return foods;
    }


}
