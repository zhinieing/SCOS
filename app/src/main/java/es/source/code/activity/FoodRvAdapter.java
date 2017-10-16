package es.source.code.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import es.source.code.model.Food;

/**
 * Created by pengming on 2017/10/13.
 */

public class FoodRvAdapter extends RecyclerView.Adapter<FoodRvAdapter.MyViewHolder> implements View.OnClickListener{
    private Context context;
    private ArrayList<Food> foods;

    private OnItemClickListener mOnItemClickListener = null;


    public static interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public FoodRvAdapter(Context context, ArrayList<Food> foods){
        this.context = context;
        this.foods = foods;
    }

    @Override
    public FoodRvAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.food_rv_layout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(itemView);
        itemView.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FoodRvAdapter.MyViewHolder holder, final int position) {
        holder.foodName.setText(foods.get(position).getFoodname());
        holder.foodPrice.setText(foods.get(position).getFoodprice());

        holder.orderFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!foods.get(position).getIsOrdered()){
                    Toast.makeText(context, "点菜成功", Toast.LENGTH_SHORT).show();
                    foods.get(position).setIsOrdered(true);
                    holder.orderFood.setText(R.string.food_return);
                } else {
                    Toast.makeText(context, "退点成功", Toast.LENGTH_SHORT).show();
                    foods.get(position).setIsOrdered(false);
                    holder.orderFood.setText(R.string.food_ordering);
                }
            }
        });

        holder.itemView.setTag(position);
    }

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(v, (int)v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, foodPrice;
        Button orderFood;

        public MyViewHolder(View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.food_name);
            foodPrice = itemView.findViewById(R.id.food_price);
            orderFood = itemView.findViewById(R.id.order_food);
        }
    }
}
