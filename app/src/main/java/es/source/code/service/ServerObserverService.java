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

import java.util.List;

public class ServerObserverService extends Service {

    private Boolean exit = false;

    private Handler cMessageHandler = new Handler(){

        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what){
                case 0:
                    exit = true;
                    break;
                case 1:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!exit){
                                String foodName = null;
                                int foodNumber = 0;

                                try{
                                    foodName = "水煮肉片1";
                                    foodNumber = 30;
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if(isAppRunning()){
                                    Message msgTo = Message.obtain();
                                    msgTo.what = 10;
                                    Bundle bundle = new Bundle();
                                    //在Bundle中通过put方法传入菜名称及库存量
                                    bundle.putString("food_name", foodName);
                                    bundle.putInt("food_number", foodNumber);
                                    msgTo.setData(bundle);

                                    try{
                                        msg.replyTo.send(msgTo);
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
