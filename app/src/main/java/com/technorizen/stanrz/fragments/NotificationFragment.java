package com.technorizen.stanrz.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.activites.HomeActivity;
import com.technorizen.stanrz.adapters.YouAdapter;
import com.technorizen.stanrz.databinding.FragmentNotificationBinding;
import com.technorizen.stanrz.models.SuccessResGetNotifications;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.NetworkAvailablity;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technorizen.stanrz.activites.LoginActivity.TAG;
import static com.technorizen.stanrz.retrofit.Constant.USER_ID;
import static com.technorizen.stanrz.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class NotificationFragment extends Fragment {

    FragmentNotificationBinding binding;
    StanrzInterface apiInterface;
    private LinkedList<SuccessResGetNotifications.Result> notificationList = new LinkedList<SuccessResGetNotifications.Result>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotificationFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_notification, container, false);
        apiInterface = ApiClient.getClient().create(StanrzInterface.class);
        Bundle bundle = this.getArguments();
        if (bundle!=null)
        {
            binding.header.layoutHeader.setVisibility(View.VISIBLE);
            binding.header.imgHeader.setOnClickListener(v -> getActivity().onBackPressed());
            binding.header.tvHeader.setText(getString(R.string.notifications));
        }
        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {
            getNotification();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }
        return binding.getRoot();
    }

    private void getNotification()
    {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResGetNotifications> call = apiInterface.getNotifications(map);
        call.enqueue(new Callback<SuccessResGetNotifications>() {
            @Override
            public void onResponse(Call<SuccessResGetNotifications> call, Response<SuccessResGetNotifications> response) {
                DataManager.getInstance().hideProgressMessage();
//                try {
//                    SuccessResAddComment data = response.body();
//                    JSONObject jsonObject = new JSONObject(response.body().string());
//                    String data = jsonObject.getString("status");
//                    String message = jsonObject.getString("message");
//                    JSONArray array = jsonObject.getJSONArray("result");
//                    if (data.equals("1")) {
//                        String dataResponse = new Gson().toJson(response.body().toString());
//                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
//                        SuccessResGetNotifications model = new Gson().fromJson(dataResponse,SuccessResGetNotifications.class);
//                        notificationList.clear();
//                        notificationList.addAll(model.getResult());
//                        binding.rvYou.setHasFixedSize(true);
//                        binding.rvYou.setLayoutManager(new LinearLayoutManager(getActivity()));
//                        binding.rvYou.setAdapter(new YouAdapter(getActivity(),notificationList));
//                        ((HomeActivity) getActivity()).getUnseenNotificationCount();
//                    } else if (data.equals("0")) {
//                        showToast(getActivity(),message);
//                        notificationList.clear();
//                        binding.rvYou.setHasFixedSize(true);
//                        binding.rvYou.setLayoutManager(new LinearLayoutManager(getActivity()));
//                        binding.rvYou.setAdapter(new YouAdapter(getActivity(),notificationList));
//                    }
//                } catch (Exception e) {
//                    Log.d(TAG, "onResponse: "+e);
//                    e.printStackTrace();
//                }
//            }
                try {
                    SuccessResGetNotifications data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        notificationList.clear();
                        notificationList.addAll(data.getResult());
                        binding.rvYou.setHasFixedSize(true);
                        binding.rvYou.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvYou.setAdapter(new YouAdapter(getActivity(),notificationList));
                        ((HomeActivity) getActivity()).getUnseenNotificationCount();
            } else if (data.status.equals("0")) {
                        notificationList.clear();
                        notificationList.addAll(data.getResult());
                        binding.rvYou.setHasFixedSize(true);
                        binding.rvYou.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvYou.setAdapter(new YouAdapter(getActivity(),notificationList));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetNotifications> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }
}