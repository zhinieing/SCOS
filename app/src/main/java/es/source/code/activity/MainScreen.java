package es.source.code.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.source.code.model.User;


public class MainScreen extends AppCompatActivity {

    private final static int REQUESTCODE = 1;
    @BindView(R.id.gv)
    GridView gv;

    MyAdapter myAdapter;

    private List<Map<String, Object>> list;

    private int images[] = {
            R.drawable.ic_restaurant_black_24dp,
            R.drawable.ic_menu_black_24dp,
            R.drawable.ic_person_black_24dp,
            R.drawable.ic_help_black_24dp
    };

    private int texts[] = {
            R.string.title_ordering,
            R.string.title_myorder,
            R.string.title_login,
            R.string.title_help
    };

    User user = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        ButterKnife.bind(this);

        list = new ArrayList<Map<String, Object>>();
        for(int i = 0; i < images.length; i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", images[i]);
            map.put("text", texts[i]);
            list.add(map);
        }

        myAdapter = new MyAdapter();

        gv.setAdapter(myAdapter);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent loginIntent;
                Intent helperIntent;

                if(parent.getCount() == 2){
                    switch (position){
                        case 0:
                            loginIntent = new Intent(MainScreen.this, LoginOrRegister.class);
                            startActivityForResult(loginIntent, REQUESTCODE);
                            break;
                        case 1:
                            helperIntent = new Intent(MainScreen.this, SCOSHelper.class);
                            startActivityForResult(helperIntent, REQUESTCODE);
                            break;
                    }
                } else {
                    switch (position){
                        case 0:
                            Intent foodViewIntent = new Intent(MainScreen.this, FoodView.class);
                            foodViewIntent.putExtra("foodViewUser", user);
                            startActivity(foodViewIntent);
                            break;
                        case 1:
                            Intent foodOrderViewIntent = new Intent(MainScreen.this, FoodOrderView.class);
                            foodOrderViewIntent.putExtra("foodOrderViewUser", user);
                            startActivity(foodOrderViewIntent);
                            break;
                        case 2:
                            loginIntent = new Intent(MainScreen.this, LoginOrRegister.class);
                            startActivityForResult(loginIntent, REQUESTCODE);
                            break;
                        case 3:
                            helperIntent = new Intent(MainScreen.this, SCOSHelper.class);
                            startActivityForResult(helperIntent, REQUESTCODE);
                            break;
                    }
                }
            }
        });


        SharedPreferences pref = getSharedPreferences("userdata", MODE_PRIVATE);
        if(pref.getInt("loginState", 0) == 0){
            list.remove(1);
            list.remove(0);
            myAdapter.notifyDataSetChanged();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(gv.getCount() == 4){
            SharedPreferences pref = getSharedPreferences("userdata", MODE_PRIVATE);
            if(pref.getInt("loginState", 0) == 0){
                list.remove(1);
                list.remove(0);
                myAdapter.notifyDataSetChanged();
            }
        }

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE) {

                user = (User) data.getSerializableExtra("user");

                if (!user.getOldUser()){
                    Toast.makeText(this, "欢迎您成为SCOS新用户", Toast.LENGTH_LONG).show();
                }

                if(gv.getCount() == 2){
                    list.clear();

                    for(int i = 0; i < images.length; i++){
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("image", images[i]);
                        map.put("text", texts[i]);
                        list.add(map);
                    }
                    myAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(MainScreen.this)
                    .inflate(R.layout.gridview_layout, parent, false);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_iv);
            imageView.setImageResource((Integer) list.get(position).get("image"));
            TextView textView = (TextView) convertView.findViewById(R.id.grid_tv);
            textView.setText((Integer) list.get(position).get("text"));

            return convertView;
        }
    }

}
