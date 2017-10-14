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
 * Created by pengming on 2017/10/14.
 */

public class FoodOrderRvAdapter extends RecyclerView.Adapter<FoodOrderRvAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Food> foods;
    private Boolean isOrdered;

    public FoodOrderRvAdapter(Context context, ArrayList<Food> foods, Boolean isOrdered){
        this.context = context;
        this.foods = foods;
        this.isOrdered = isOrdered;
    }


    @Override
    public FoodOrderRvAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        if(isOrdered)
            itemView = LayoutInflater.from(context).inflate(R.layout.food_order_rvright_layout, parent, false);
        else
            itemView = LayoutInflater.from(context).inflate(R.layout.food_order_rvleft_layout, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if(isOrdered){
            holder.foodNameRight.setText(foods.get(position).getFoodname());
            holder.foodPriceRight.setText(foods.get(position).getFoodprice());
            holder.foodNumberRight.setText(String.valueOf(foods.get(position).getNumber()));
        } else {
            holder.foodNameLeft.setText(foods.get(position).getFoodname());
            holder.foodPriceLeft.setText(foods.get(position).getFoodprice());
            holder.foodNumberLeft.setText(String.valueOf(foods.get(position).getNumber()));
        }
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView foodNameLeft, foodPriceLeft, foodNumberLeft;
        Button returnFood;

        TextView foodNameRight, foodPriceRight, foodNumberRight;

        public MyViewHolder(View itemView) {
            super(itemView);

            if(isOrdered){
                foodNameRight = (TextView) itemView.findViewById(R.id.food_name_right);
                foodPriceRight = (TextView) itemView.findViewById(R.id.food_price_right);
                foodNumberRight = (TextView) itemView.findViewById(R.id.food_number_right);
            } else {
                foodNameLeft = (TextView) itemView.findViewById(R.id.food_name_left);
                foodPriceLeft = (TextView) itemView.findViewById(R.id.food_price_left);
                foodNumberLeft = (TextView) itemView.findViewById(R.id.food_number_left);

                returnFood = (Button) itemView.findViewById(R.id.return_food);
                returnFood.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(returnFood.getText().toString().equals("退点")){
                            Toast.makeText(context, "退点成功", Toast.LENGTH_SHORT).show();
                            returnFood.setText(R.string.food_ordering);
                        } else {
                            Toast.makeText(context, "点菜成功", Toast.LENGTH_SHORT).show();
                            returnFood.setText(R.string.food_return);
                        }
                    }
                });
            }
        }
    }
}