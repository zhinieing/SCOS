package es.source.code.activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import es.source.code.model.Food;


/**
 * Created by pengming on 2017/10/15.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private ArrayList<Food> foods;

    public ImageAdapter(ArrayList<Food> foods){
        this.foods = foods;
    }

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ImageAdapter.ViewHolder holder, int position) {
        Picasso.with(holder.itemView.getContext())
                .load(foods.get(position).getImageurl())
                .fit()
                .centerCrop()
                .into(holder.foodImage);
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;

        public ViewHolder(View itemView) {
            super(itemView);

            foodImage = itemView.findViewById(R.id.food_image);
        }
    }
}
