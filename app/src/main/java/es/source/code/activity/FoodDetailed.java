package es.source.code.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.source.code.model.Food;

public class FoodDetailed extends AppCompatActivity{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.item_picker)
    HorizontalInfiniteCycleViewPager itemPicker;

    private ArrayList<Food> foods;
    private int position;
    private ImagePageAdapter imagePageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_detailed);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        foods = (ArrayList<Food>) getIntent().getSerializableExtra("foods");
        position = getIntent().getIntExtra("position", 0);

        imagePageAdapter = new ImagePageAdapter(this, foods);
        itemPicker.setAdapter(imagePageAdapter);

        itemPicker.setCurrentItem(position);

    }

}
