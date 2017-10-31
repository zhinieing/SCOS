package es.source.code.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;

public class SCOSEntry extends AppCompatActivity {

    @BindView(R.id.scos_launch_image)
    ImageView mLaunchImageView;

    int lastX = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry);
        ButterKnife.bind(this);


        RequestOptions options = new RequestOptions();
        options.centerCrop();

        Glide.with(this)
                .load(R.drawable.launcher)
                .apply(options)
                .into(mLaunchImageView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                break;
            case MotionEvent.ACTION_UP:
                int offsetX = x - lastX;
                if (offsetX < -5) {
                    Intent intent = new Intent(this, MainScreen.class);
                    startActivity(intent);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences.Editor editor = getSharedPreferences("userdata", MODE_PRIVATE).edit();
        editor.putLong("lastTime", System.currentTimeMillis());
        editor.apply();
    }
}
