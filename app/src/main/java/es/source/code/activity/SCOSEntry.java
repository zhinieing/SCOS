package es.source.code.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SCOSEntry extends AppCompatActivity {

    @BindView(R.id.scos_launch_image)
    ImageView mLaunchImageView;

    int lastX = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry);
        ButterKnife.bind(this);


        Picasso.with(this).load(R.drawable.launcher).fit().centerCrop().into(mLaunchImageView);
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
}
