package es.source.code.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
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
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.source.code.model.Food;
import es.source.code.model.User;
import es.source.code.service.ServerObserverService;

public class FoodView extends AppCompatActivity {


    @BindView(R.id.toolbar_food)
    Toolbar toolbar;
    @BindView(R.id.tabs_food)
    TabLayout tabLayout;
    @BindView(R.id.container_food)
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

    private static final String START_ASYNCSERVICE = "启动实时更新";

    User user;


    String foodName;
    int foodNumber;
    private Handler sMessageHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 10:
                    foodName = msg.getData().getString("food_name");
                    foodNumber = msg.getData().getInt("food_number");

                    PlaceholderFragment fragment = (PlaceholderFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.container_food+":0");
                    if(fragment != null){
                        fragment.dataChanged(foodName, foodNumber);
                    }

                    break;

                default:
            }
        }
    };


    private Messenger cMessenger = new Messenger(sMessageHandler);


    private Boolean exit = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Messenger sMessenger = new Messenger(service);

            Message message = Message.obtain();
            if(!exit){
                message.what = 1;
            } else {
                message.what = 0;
            }
            message.replyTo = cMessenger;

            try{
                sMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_view);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout.setupWithViewPager(mViewPager);


        try{
            user = (User) getIntent().getSerializableExtra("foodViewUser");
        } catch (Exception e){

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_food_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.ordered_food:
                Intent orderedFood = new Intent(this, FoodOrderView.class);
                orderedFood.putExtra("startPage", 0);
                orderedFood.putExtra("foodViewUser", user);
                startActivity(orderedFood);
                break;
            case R.id.look_order:
                Intent lookOrder = new Intent(this, FoodOrderView.class);
                lookOrder.putExtra("startPage", 1);
                lookOrder.putExtra("foodViewUser", user);
                startActivity(lookOrder);
                break;
            case R.id.call_service:

                break;
            case R.id.start_asyncservice:
                if(START_ASYNCSERVICE.equals(item.getTitle())){
                    Intent service = new Intent(FoodView.this, ServerObserverService.class);
                    bindService(service, serviceConnection, BIND_AUTO_CREATE);

                    item.setTitle(R.string.stop_asyncservice);
                } else {
                    exit = true;
                    item.setTitle(R.string.start_asyncservice);
                }
                break;
            default:
        }

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

        FoodRvAdapter foodAdapter;
        ArrayList<Food> foods;

        @BindView(R.id.food_list)
        RecyclerView foodList;
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
        public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_food_view, container, false);
            unbinder = ButterKnife.bind(this, rootView);

            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            foodList.setLayoutManager(llm);
            foodList.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));

            foods = new ArrayList<Food>();
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    for (int i = 0; i < 15; i++) {
                        Food food1 = new Food("夫妻肺片" + i % 10, "2" + i % 10, R.drawable.food1, "", 10, 0, false);
                        foods.add(food1);
                    }
                    break;
                case 2:
                    for (int i = 0; i < 15; i++) {
                        Food food2 = new Food("水煮肉片" + i % 10, "3" + i % 10, R.drawable.food2, "", 10, 0, false);
                        foods.add(food2);
                    }
                    break;
                case 3:
                    for (int i = 0; i < 15; i++) {
                        Food food3 = new Food("蛤蜊" + i % 10, "4" + i % 10, R.drawable.food3, "", 10, 0, false);
                        foods.add(food3);
                    }
                    break;
                case 4:
                    for (int i = 0; i < 15; i++) {
                        Food food4 = new Food("勇闯天涯" + i % 10, "1" + i % 10, R.drawable.food4, "", 10, 0, false);
                        foods.add(food4);
                    }
                    break;
            }

            foodAdapter = new FoodRvAdapter(container.getContext(), foods);
            foodList.setAdapter(foodAdapter);

            foodAdapter.setOnItemClickListener(new FoodRvAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    Intent foodDetail = new Intent(view.getContext(), FoodDetailed.class);
                    foodDetail.putExtra("foods", foods);
                    foodDetail.putExtra("position", position);

                    startActivity(foodDetail);
                }
            });

            return rootView;
        }

        public void dataChanged(String foodName, int foodNumber){

            for(Food food : foods){
                if(foodName.equals(food.getFoodname())){
                    food.setNumber(foodNumber);
                }
            }
            foodAdapter.notifyDataSetChanged();
        }


        @Override
        public void onDestroyView() {
            super.onDestroyView();
            unbinder.unbind();
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
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "冷菜";
                case 1:
                    return "热菜";
                case 2:
                    return "海鲜";
                case 3:
                    return "酒水";
            }
            return null;
        }
    }
}
