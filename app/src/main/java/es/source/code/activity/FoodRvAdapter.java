package es.source.code.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import es.source.code.model.Food;

/**
 * Created by pengming on 2017/10/13.
 */

public class FoodRvAdapter extends RecyclerView.Adapter<FoodRvAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<Food> foods;

    public FoodRvAdapter(Context context, ArrayList<Food> foods){
        this.context = context;
        this.foods = foods;
    }

    @Override
    public FoodRvAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.food_rv_layout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FoodRvAdapter.MyViewHolder holder, int position) {
        holder.foodName.setText(foods.get(position).getFoodname());
        holder.foodPrice.setText(foods.get(position).getFoodprice());

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
            foodName = (TextView) itemView.findViewById(R.id.food_name);
            foodPrice = (TextView) itemView.findViewById(R.id.food_price);
            orderFood = (Button) itemView.findViewById(R.id.order_food);
            orderFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(orderFood.getText().toString().equals("点菜")){
                        Toast.makeText(context, "点菜成功", Toast.LENGTH_SHORT).show();
                        orderFood.setText(R.string.food_return);
                    } else {
                        Toast.makeText(context, "退点成功", Toast.LENGTH_SHORT).show();
                        orderFood.setText(R.string.food_ordering);
                    }

                }
            });
        }
    }
}
