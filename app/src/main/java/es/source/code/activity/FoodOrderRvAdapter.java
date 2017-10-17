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
    private Boolean isInOrderList;

    public FoodOrderRvAdapter(Context context, ArrayList<Food> foods, Boolean isInOrderList){
        this.context = context;
        this.foods = foods;
        this.isInOrderList = isInOrderList;
    }


    @Override
    public FoodOrderRvAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        if(isInOrderList) {
            itemView = LayoutInflater.from(context).inflate(R.layout.food_order_rvright_layout, parent, false);
        } else {
            itemView = LayoutInflater.from(context).inflate(R.layout.food_order_rvleft_layout, parent, false);
        }

        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if(isInOrderList){
            holder.foodNameRight.setText(foods.get(position).getFoodname());
            holder.foodPriceRight.setText(foods.get(position).getFoodprice());
            holder.foodNumberRight.setText(String.valueOf(foods.get(position).getNumber()));
            holder.foodCommentRight.setText(foods.get(position).getComment());
        } else {
            holder.foodNameLeft.setText(foods.get(position).getFoodname());
            holder.foodPriceLeft.setText(foods.get(position).getFoodprice());
            holder.foodNumberLeft.setText(String.valueOf(foods.get(position).getNumber()));
            holder.foodCommentLeft.setText(foods.get(position).getComment());

            holder.returnFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!foods.get(position).getIsOrdered()){
                        Toast.makeText(context, "点菜成功", Toast.LENGTH_SHORT).show();
                        foods.get(position).setIsOrdered(true);
                        holder.returnFood.setText(R.string.food_return);
                    } else {
                        Toast.makeText(context, "退点成功", Toast.LENGTH_SHORT).show();
                        foods.get(position).setIsOrdered(false);
                        holder.returnFood.setText(R.string.food_ordering);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView foodNameLeft, foodPriceLeft, foodNumberLeft, foodCommentLeft;
        Button returnFood;

        TextView foodNameRight, foodPriceRight, foodNumberRight, foodCommentRight;

        public MyViewHolder(View itemView) {
            super(itemView);

            if(isInOrderList){
                foodNameRight = itemView.findViewById(R.id.food_name_right);
                foodPriceRight = itemView.findViewById(R.id.food_price_right);
                foodNumberRight = itemView.findViewById(R.id.food_number_right);
                foodCommentRight = itemView.findViewById(R.id.food_comment_right);
            } else {
                foodNameLeft = itemView.findViewById(R.id.food_name_left);
                foodPriceLeft = itemView.findViewById(R.id.food_price_left);
                foodNumberLeft = itemView.findViewById(R.id.food_number_left);
                foodCommentLeft = itemView.findViewById(R.id.food_comment_left);

                returnFood = itemView.findViewById(R.id.return_food);
            }
        }
    }
}
