package es.source.code.activity;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;

/**
 * Created by pengming on 2017/10/31.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        Bmob.initialize(MyApplication.getContext(), "8872db8c5c84f6c51d85dc66e35930b2", "bmob");
    }

    public static Context getContext(){
        return context;
    }

}
