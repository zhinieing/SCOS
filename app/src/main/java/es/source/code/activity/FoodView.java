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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import es.source.code.model.Food;
import es.source.code.model.Msg;
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



    /*private Handler sMessageHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 10:

                    Food food = (Food) msg.getData().getSerializable("Food");

                    int index;
                    switch (food.getKind()){
                        case "冷菜":
                            index = 0;
                            break;
                        case "热菜":
                            index = 1;
                            break;
                        case "海鲜":
                            index = 2;
                            break;
                        case "酒水":
                            index = 3;
                            break;
                        default:
                            index = 0;

                    }

                    PlaceholderFragment fragment = (PlaceholderFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.container_food+":"+index);
                    if(fragment != null){
                        fragment.dataChanged(food);
                    }

                    break;

                default:
            }
        }
    };

    private Messenger cMessenger = new Messenger(sMessageHandler);



    private Messenger sMessenger;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            sMessenger = new Messenger(service);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };*/


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateFoodView(Food event) {
        int index;
        switch (event.getKind()){
            case "冷菜":
                index = 0;
                break;
            case "热菜":
                index = 1;
                break;
            case "海鲜":
                index = 2;
                break;
            case "酒水":
                index = 3;
                break;
            default:
                index = 0;

        }

        PlaceholderFragment fragment = (PlaceholderFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.container_food+":"+index);
        if(fragment != null){
            fragment.dataChanged(event);
        }
    }



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

        Intent service = new Intent(FoodView.this, ServerObserverService.class);
        startService(service);
        //bindService(service, serviceConnection, BIND_AUTO_CREATE);


        EventBus.getDefault().register(this);

        try{
            user = (User) getIntent().getSerializableExtra("foodViewUser");
        } catch (Exception e){

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_food_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
                    item.setTitle(R.string.stop_asyncservice);

                    /*Message message = Message.obtain();
                    message.what = 1;

                    message.replyTo = cMessenger;

                    try{
                        sMessenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }*/

                    EventBus.getDefault().post(new Msg(1));


                } else {
                    item.setTitle(R.string.start_asyncservice);

                    /*Message message = Message.obtain();
                    message.what = 0;

                    message.replyTo = cMessenger;

                    try{
                        sMessenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }*/

                    EventBus.getDefault().post(new Msg(0));
                }
                break;
            default:
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unbindService(serviceConnection);
        Intent service = new Intent(FoodView.this, ServerObserverService.class);
        stopService(service);
        EventBus.getDefault().unregister(this);
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
            foodAdapter = new FoodRvAdapter(container.getContext(), foods);
            foodList.setAdapter(foodAdapter);


            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    BmobQuery<Food> query1 = new BmobQuery<Food>();
                    query1.addWhereEqualTo("kind", "冷菜");
                    query1.order("-createdAt");
                    query1.setLimit(30);
                    query1.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
                    query1.findObjects(new FindListener<Food>() {
                        @Override
                        public void done(List<Food> list, BmobException e) {
                            for(Food food : list){
                                foods.add(food);
                            }
                            foodAdapter.notifyDataSetChanged();
                        }
                    });
                    break;

                case 2:
                    BmobQuery<Food> query2 = new BmobQuery<Food>();
                    query2.addWhereEqualTo("kind", "热菜");
                    query2.order("-createdAt");
                    query2.setLimit(30);
                    query2.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
                    query2.findObjects(new FindListener<Food>() {
                        @Override
                        public void done(List<Food> list, BmobException e) {
                            for(Food food : list){
                                foods.add(food);
                            }
                            foodAdapter.notifyDataSetChanged();
                        }
                    });
                    break;

                case 3:
                    BmobQuery<Food> query3 = new BmobQuery<Food>();
                    query3.addWhereEqualTo("kind", "海鲜");
                    query3.order("-createdAt");
                    query3.setLimit(30);
                    query3.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
                    query3.findObjects(new FindListener<Food>() {
                        @Override
                        public void done(List<Food> list, BmobException e) {
                            for(Food food : list){
                                foods.add(food);
                            }
                            foodAdapter.notifyDataSetChanged();
                        }
                    });
                    break;

                case 4:
                    BmobQuery<Food> query4 = new BmobQuery<Food>();
                    query4.addWhereEqualTo("kind", "酒水");
                    query4.order("-createdAt");
                    query4.setLimit(30);
                    query4.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
                    query4.findObjects(new FindListener<Food>() {
                        @Override
                        public void done(List<Food> list, BmobException e) {
                            for(Food food : list){
                                foods.add(food);
                            }
                            foodAdapter.notifyDataSetChanged();
                        }
                    });
                    break;

                default:
            }



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

        public void dataChanged(Food food){
            foods.add(food);
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
