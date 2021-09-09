package com.technorizen.stanrz.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.util.SharedPreferencesUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.adapters.FollowingAdapter;
import com.technorizen.stanrz.adapters.OtherUserFollowingAdapter;
import com.technorizen.stanrz.databinding.FragmentOtherUserFollowersFollowingBinding;
import com.technorizen.stanrz.models.SuccessResAddFollowing;
import com.technorizen.stanrz.models.SuccessResGetFollowings;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.AddFollow;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technorizen.stanrz.retrofit.Constant.USER_ID;
import static com.technorizen.stanrz.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OtherUserFollowersFollowingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OtherUserFollowersFollowingFragment extends Fragment implements AddFollow {


    FragmentOtherUserFollowersFollowingBinding binding;
    StanrzInterface apiInterface;
    private List<SuccessResGetFollowings.Result> followersList = new LinkedList<>();

    private String otherUserID;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OtherUserFollowersFollowingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OtherUserFollowersFollowingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OtherUserFollowersFollowingFragment newInstance(String param1, String param2) {
        OtherUserFollowersFollowingFragment fragment = new OtherUserFollowersFollowingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_other_user_followers_following, container, false);

        binding.header.imgHeader.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        apiInterface = ApiClient.getClient().create(StanrzInterface.class);


        binding.tabLayoutEventDay.addTab(binding.tabLayoutEventDay.newTab().setText(R.string.followers));
        binding.tabLayoutEventDay.addTab(binding.tabLayoutEventDay.newTab().setText(R.string.following));
        binding.tabLayoutEventDay.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.tabLayoutEventDay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int currentTabSelected= tab.getPosition();
                if(currentTabSelected==0)
                {
                    //Go for Today

                    getFollowers();

                }else if(currentTabSelected==1)
                {
                    //Go for Upcoming
                    getFollowings();

                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });



        Bundle bundle = this.getArguments();

        if (bundle!=null)
        {
            String name = bundle.getString("name");
            otherUserID =  bundle.getString("id");
            binding.header.tvHeader.setText(name);
            String Goto = bundle.getString("Goto");


            if(Goto.equalsIgnoreCase("1"))
            {
                binding.tabLayoutEventDay.getTabAt(0).select();
                getFollowers();

            } else
            {
                binding.tabLayoutEventDay.getTabAt(1).select();
                getFollowings();
            }
        }


        return binding.getRoot();
    }
    private void getFollowers()
    {

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("other_id",otherUserID);

      /*  RequestBody email = RequestBody.create(MediaType.parse("text/plain"),strEmail);
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), strPassword);
        RequestBody registerID = RequestBody.create(MediaType.parse("text/plain"),deviceToken);
*/
//        Call<SuccessResSignIn> call = apiInterface.login(email,password,registerID);
        Call<SuccessResGetFollowings> call = apiInterface.getOthersFollower(map);

        call.enqueue(new Callback<SuccessResGetFollowings>() {
            @Override
            public void onResponse(Call<SuccessResGetFollowings> call, Response<SuccessResGetFollowings> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResGetFollowings data = response.body();
//                    setSellerData();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        followersList.clear();
                        followersList.addAll(data.getResult());

                        binding.rvFollowing.setHasFixedSize(true);
                        binding.rvFollowing.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvFollowing.setAdapter(new OtherUserFollowingAdapter(getActivity(),followersList,OtherUserFollowersFollowingFragment.this));

//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                        followersList.clear();
                        binding.rvFollowing.setHasFixedSize(true);
                        binding.rvFollowing.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvFollowing.setAdapter(new OtherUserFollowingAdapter(getActivity(),followersList,OtherUserFollowersFollowingFragment.this));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetFollowings> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void getFollowings()
    {

        String userID = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userID);
        map.put("other_id",otherUserID);

      /*  RequestBody email = RequestBody.create(MediaType.parse("text/plain"),strEmail);
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), strPassword);
        RequestBody registerID = RequestBody.create(MediaType.parse("text/plain"),deviceToken);
*/
//        Call<SuccessResSignIn> call = apiInterface.login(email,password,registerID);
        Call<SuccessResGetFollowings> call = apiInterface.getOthersFollowing(map);

        call.enqueue(new Callback<SuccessResGetFollowings>() {
            @Override
            public void onResponse(Call<SuccessResGetFollowings> call, Response<SuccessResGetFollowings> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResGetFollowings data = response.body();
//                    setSellerData();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        followersList.clear();
                        followersList.addAll(data.getResult());

                        binding.rvFollowing.setHasFixedSize(true);
                        binding.rvFollowing.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvFollowing.setAdapter(new OtherUserFollowingAdapter(getActivity(),followersList,OtherUserFollowersFollowingFragment.this));

//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                        followersList.clear();
                        binding.rvFollowing.setHasFixedSize(true);
                        binding.rvFollowing.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvFollowing.setAdapter(new OtherUserFollowingAdapter(getActivity(),followersList,OtherUserFollowersFollowingFragment.this));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetFollowings> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }
    @Override
    public void addFollow(String othersId) {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("follower_id",othersId);

      /*  RequestBody email = RequestBody.create(MediaType.parse("text/plain"),strEmail);
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), strPassword);
        RequestBody registerID = RequestBody.create(MediaType.parse("text/plain"),deviceToken);
*/
//        Call<SuccessResSignIn> call = apiInterface.login(email,password,registerID);
        Call<SuccessResAddFollowing> call = apiInterface.addFollowing(map);

        call.enqueue(new Callback<SuccessResAddFollowing>() {
            @Override
            public void onResponse(Call<SuccessResAddFollowing> call, Response<SuccessResAddFollowing> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResAddFollowing data = response.body();
//                    setSellerData();
                    Log.e("data",data.status);
                    showToast(getActivity(), data.result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResAddFollowing> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }
}