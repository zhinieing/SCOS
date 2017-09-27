package es.source.code.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainScreen extends AppCompatActivity {

    private final static int REQUESTCODE = 1;

    @BindView(R.id.message)
    TextView mTextMessage;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_ordering:
                    mTextMessage.setText(R.string.title_ordering);
                    return true;
                case R.id.navigation_myorder:
                    mTextMessage.setText(R.string.title_myorder);
                    return true;
                case R.id.navigation_login:
                    Intent loginIntent = new Intent(MainScreen.this, LoginOrRegister.class);
                    startActivityForResult(loginIntent, REQUESTCODE);
                    return true;
                case R.id.navigation_help:
                    mTextMessage.setText(R.string.title_help);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        ButterKnife.bind(this);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        String message = getIntent().getStringExtra(SCOSEntry.EXTRA_MESSAGE);
        if (!message.equals("FromEntry")) {
            navigation.getMenu().removeItem(R.id.navigation_ordering);
            navigation.getMenu().removeItem(R.id.navigation_myorder);
        }else{
            disableShiftMode(navigation);
        }

    }

    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 2){
            if(requestCode == REQUESTCODE){
                String returnMessage = data.getStringExtra(LoginOrRegister.RETURN_TAG);
                //Toast.makeText(getApplicationContext(), returnMessage, Toast.LENGTH_LONG).show();

                if(returnMessage.equals("LoginSuccess")){
                    if(navigation.getMenu().size() == 2){
                        navigation.getMenu().add(0, R.id.navigation_ordering, 0, R.string.title_ordering)
                                .setIcon(R.drawable.ic_restaurant_black_24dp);
                        navigation.getMenu().add(0, R.id.navigation_myorder, 1, R.string.title_myorder)
                                .setIcon(R.drawable.ic_menu_black_24dp);
                        disableShiftMode(navigation);
                    }
                }
            }
        }
    }

}
