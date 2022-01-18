package com.technorizen.stanrz.activites;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.models.SuccessResGetUnseenNotification;
import com.technorizen.stanrz.models.SuccessResProfileData;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;
import com.technorizen.stanrz.utility.Util;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technorizen.stanrz.retrofit.Constant.USER_ID;
import static com.technorizen.stanrz.retrofit.Constant.showToast;

public class HomeActivity extends AppCompatActivity {

    NavController navController;

    private StanrzInterface apiInterface;;

    private Bundle intent;

    private String status ="";

    LocalBroadcastManager lbm;
    BottomNavigationView navView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_add,R.id.navigation_notification,R.id.navigation_profile)
                .build();
         navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        apiInterface = ApiClient.getClient().create(StanrzInterface.class);

        intent =  getIntent().getExtras();
        if(intent!=null)
        {

            String key = intent.getString("key");

            status=  intent.getString("notification");

            if (status.equalsIgnoreCase("notification")){
                navController.navigateUp();
                Bundle bundle = new Bundle();
                navController.navigate(R.id.navigation_notification,bundle);
            }
        }

        lbm = LocalBroadcastManager.getInstance(this);
        lbm.registerReceiver(receiver, new IntentFilter("filter_string"));

        getUnseenNotificationCount();

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String str = intent.getStringExtra("key");
                // get all your data from intent and do what you want
                getUnseenNotificationCount();

                NavDestination dat = navController.getCurrentDestination();
                int id = dat.getId();

                if(R.id.navigation_home ==  id)
                {
                    if(Util.appInForeground(HomeActivity.this))
                    {
                        Intent intent1 = new Intent("filter_string1");
                        intent.putExtra("key", "My Data");
                        // put your all data using put extra
                        LocalBroadcastManager.getInstance(HomeActivity.this).sendBroadcast(intent1);
                    }
                }
                }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);

    }

    public  void getUnseenNotificationCount()
    {

        String userId = SharedPreferenceUtility.getInstance(this).getString(USER_ID);
    //    DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetUnseenNotification> call = apiInterface.getUnseenNoti(map);

        call.enqueue(new Callback<SuccessResGetUnseenNotification>() {
            @Override
            public void onResponse(Call<SuccessResGetUnseenNotification> call, Response<SuccessResGetUnseenNotification> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResGetUnseenNotification data = response.body();
//                    setSellerData();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        int unseenNoti = data.getResult().getTotalUnseenNotification();

                        if(unseenNoti!=0)
                        {
                            navView.getOrCreateBadge(R.id.navigation_notification).setNumber(unseenNoti);
                            navView.getOrCreateBadge(R.id.navigation_notification).setVisible(true);
                        }
                        else
                        {
                            navView.getOrCreateBadge(R.id.navigation_notification).setVisible(false);
                        }

                    } else if (data.status.equals("0")) {
                       // showToast(this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetUnseenNotification> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

}