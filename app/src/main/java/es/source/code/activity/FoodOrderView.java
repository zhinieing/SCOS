package es.source.code.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.source.code.model.Food;
import es.source.code.model.User;

public class FoodOrderView extends AppCompatActivity {

    @BindView(R.id.toolbar_food_order)
    Toolbar toolbar;
    @BindView(R.id.tabs_food_order)
    TabLayout tabLayout;
    @BindView(R.id.container_food_order)
    ViewPager mViewPager;
    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_order_view);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout.setupWithViewPager(mViewPager);


        try {
            int startPage = getIntent().getIntExtra("startPage", 0);
            mViewPager.setCurrentItem(startPage);
        } catch (Exception e) {

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_food_order_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        FoodOrderRvAdapter foodOrderAdapter;
        ArrayList<Food> foods;

        User user;

        @BindView(R.id.food_order_list)
        RecyclerView foodOrderList;
        @BindView(R.id.order_number)
        TextView orderNumber;
        @BindView(R.id.order_price)
        TextView orderPrice;
        @BindView(R.id.order_submit)
        Button orderSubmit;
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        Unbinder unbinder;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_food_order_view, container, false);
            unbinder = ButterKnife.bind(this, rootView);

            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            foodOrderList.setLayoutManager(llm);
            foodOrderList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

            foods = new ArrayList<Food>();
            for (int i = 0; i < 15; i++) {
                Food food1 = new Food("干锅包菜" + i % 10, "2" + i % 10, R.drawable.food4, "这是一道热菜", 10, 0, false);
                foods.add(food1);
            }


            try {
                user = (User) getActivity().getIntent().getSerializableExtra("foodOrderViewUser");
            } catch (Exception e) {

            }

            if (user == null) {
                try {
                    user = (User) getActivity().getIntent().getSerializableExtra("foodViewUser");
                } catch (Exception e) {

                }
            }

            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    orderSubmit.setText(R.string.order_submit);
                    foodOrderAdapter = new FoodOrderRvAdapter(container.getContext(), foods, false);
                    foodOrderList.setAdapter(foodOrderAdapter);
                    break;
                case 2:
                    orderSubmit.setText(R.string.order_pay);
                    orderSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (user != null) {
                                if (user.getOldUser())
                                    Toast.makeText(v.getContext(), "您好，老顾客，本次你可享受7折优惠", Toast.LENGTH_SHORT).show();
                            }

                            OrderPayTask orderPayTask = new OrderPayTask();
                            orderPayTask.execute((Void) null);
                        }
                    });
                    foodOrderAdapter = new FoodOrderRvAdapter(container.getContext(), foods, true);
                    foodOrderList.setAdapter(foodOrderAdapter);
                    break;
            }

            return rootView;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            unbinder.unbind();
        }



        public class OrderPayTask extends AsyncTask<Void, Integer, Boolean>{

            @Override
            protected void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Boolean doInBackground(Void... params) {

                for(int i = 0; i < 100; i++){
                    if(isCancelled()){
                        break;
                    }
                    publishProgress(i);

                    try{
                        Thread.sleep(60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                return true;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                if(isCancelled()){
                    return;
                }

                progressBar.setProgress(values[0]);
            }

            @Override
            protected void onPostExecute(Boolean success) {
                progressBar.setVisibility(View.GONE);

                if(success){
                    orderSubmit.setEnabled(false);
                    Toast.makeText(getContext(), "本次结账金额为：", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "未下单菜";
                case 1:
                    return "已下单菜";
            }
            return null;
        }
    }
}
