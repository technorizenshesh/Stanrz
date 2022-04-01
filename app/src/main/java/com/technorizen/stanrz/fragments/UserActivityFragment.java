package com.technorizen.stanrz.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.core.NotInFilter;
import com.google.gson.Gson;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.adapters.NotificationAdapter;
import com.technorizen.stanrz.adapters.TopAllStanrzAdapter;
import com.technorizen.stanrz.databinding.FragmentUserActivityBinding;
import com.technorizen.stanrz.models.SuccessResGetStanrzOf;
import com.technorizen.stanrz.models.SuccessResGetUserActivity;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technorizen.stanrz.retrofit.Constant.USER_ID;
import static com.technorizen.stanrz.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserActivityFragment extends Fragment {

    FragmentUserActivityBinding binding;
    private StanrzInterface apiInterface;
    private ArrayList<SuccessResGetUserActivity.Result> userActivityList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public UserActivityFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static UserActivityFragment newInstance(String param1, String param2) {
        UserActivityFragment fragment = new UserActivityFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_activity, container, false);
        apiInterface = ApiClient.getClient().create(StanrzInterface.class);
        binding.header.imgHeader.setOnClickListener(v -> getActivity().onBackPressed());
        binding.header.tvHeader.setText(R.string.user_activity);
        getUserAct();
        return binding.getRoot();
    }

    public void getUserAct()
    {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResGetUserActivity> call = apiInterface.getUserActivity(map);
        call.enqueue(new Callback<SuccessResGetUserActivity>() {
            @Override
            public void onResponse(Call<SuccessResGetUserActivity> call, Response<SuccessResGetUserActivity> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetUserActivity data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        userActivityList.clear();
                        userActivityList.addAll(data.getResult());
                        binding.rvUserAct.setHasFixedSize(true);
                        binding.rvUserAct.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvUserAct.setAdapter(new NotificationAdapter(getActivity(),userActivityList));
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                        userActivityList.clear();
                        binding.rvUserAct.setHasFixedSize(true);
                        binding.rvUserAct.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvUserAct.setAdapter(new NotificationAdapter(getActivity(),userActivityList));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetUserActivity> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }
}