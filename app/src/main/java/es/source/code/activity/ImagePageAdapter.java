package es.source.code.activity;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import es.source.code.model.Food;

/**
 * Created by pengming on 2017/10/16.
 */

public class ImagePageAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<Food> foods;

    public ImagePageAdapter(Context context, ArrayList<Food> foods) {
        this.context = context;
        this.foods = foods;
    }


    @Override
    public int getCount() {
        return foods.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, container, false);

        TextView itemName = view.findViewById(R.id.item_name);
        TextView itemPrice = view.findViewById(R.id.item_price);
        ImageView foodImage = view.findViewById(R.id.food_image);
        final Button orderStatus = view.findViewById(R.id.order_status);
        final EditText editComment = view.findViewById(R.id.edit_comment);
        Button submitComment = view.findViewById(R.id.submit_comment);

        itemName.setText(foods.get(position).getFoodname());
        itemPrice.setText("¥" + foods.get(position).getFoodprice());
        Picasso.with(context)
                .load(foods.get(position).getImageurl())
                .fit()
                .centerCrop()
                .into(foodImage);


        if(!foods.get(position).getIsOrdered()){
            orderStatus.setText(R.string.food_ordering);
        } else {
            orderStatus.setText(R.string.food_return);
        }

        orderStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!foods.get(position).getIsOrdered()){
                    Toast.makeText(context, "点菜成功", Toast.LENGTH_SHORT).show();
                    foods.get(position).setIsOrdered(true);
                    orderStatus.setText(R.string.food_return);
                } else {
                    Toast.makeText(context, "退点成功", Toast.LENGTH_SHORT).show();
                    foods.get(position).setIsOrdered(false);
                    orderStatus.setText(R.string.food_ordering);
                }
            }
        });

        submitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editComment.getText().toString() != null){
                    foods.get(position).setComment(editComment.getText().toString());
                } else {
                    Toast.makeText(context, "写一些备注吧！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        container.addView(view);

        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
