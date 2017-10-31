package es.source.code.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;


import org.json.JSONObject;

import java.util.List;

import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.listener.ValueEventListener;
import es.source.code.model.Food;

public class ServerObserverService extends Service {

    private Boolean exit = false;

    private Messenger cMessenger;


    private Handler cMessageHandler = new Handler(){

        @Override
        public void handleMessage(final Message msg) {
            cMessenger = msg.replyTo;
            switch (msg.what){
                case 0:

                    exit = true;

                    break;
                case 1:
                    exit = false;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!exit){

                                Food food = null;

                                try{
                                    food = new Food("凉拌海带丝", 18, "", "",
                                            "冷菜", 20, 0, false);
                                    Thread.sleep(300);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                if(food != null && isAppRunning()){
                                    Message message = Message.obtain();
                                    message.what = 10;
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("Food", food);
                                    message.setData(bundle);

                                    try{
                                        cMessenger.send(message);
                                    } catch (RemoteException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }).start();
                    break;
                default:
            }
        }
    };


    public Boolean isAppRunning(){
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
        if(list == null){
            return false;
        }
        for(ActivityManager.RunningAppProcessInfo processInfo : list){
            if(processInfo.processName.equals(getPackageName())
                    && processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                return true;
            }
        }
        return false;
    }


    private Messenger sMessenger = new Messenger(cMessageHandler);

    public ServerObserverService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sMessenger.getBinder();
    }
}
