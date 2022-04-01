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
import com.google.gson.Gson;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.adapters.MessageAdapter;
import com.technorizen.stanrz.adapters.MyPurchasedSubscriptionAdapter;
import com.technorizen.stanrz.databinding.FragmentMyPurchasedSubscriptionBinding;
import com.technorizen.stanrz.models.SuccessResCancelSubscription;
import com.technorizen.stanrz.models.SuccessResGetConversation;
import com.technorizen.stanrz.models.SuccessResGetMyPurchasedPlan;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.NetworkAvailablity;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.AddFollow;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static android.content.ContentValues.TAG;
import static com.technorizen.stanrz.retrofit.Constant.USER_ID;
import static com.technorizen.stanrz.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyPurchasedSubscriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class MyPurchasedSubscriptionFragment extends Fragment implements AddFollow {

    FragmentMyPurchasedSubscriptionBinding binding;
    private StanrzInterface apiInterface;
    private ArrayList<SuccessResGetMyPurchasedPlan.Result> purchasedPlan = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyPurchasedSubscriptionFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyPurchasedSubscriptionFragment newInstance(String param1, String param2) {
        MyPurchasedSubscriptionFragment fragment = new MyPurchasedSubscriptionFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_purchased_subscription, container, false);
        apiInterface = ApiClient.getClient().create(StanrzInterface.class);
        binding.header.tvHeader.setText(R.string.my_purchased);
        binding.header.imgHeader.setOnClickListener(v ->
                {
                    getActivity().onBackPressed();
                }
        );
        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {
            getubscriptionPlan();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }
        return binding.getRoot();
    }

    public void getubscriptionPlan()
    {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResGetMyPurchasedPlan> call = apiInterface.getUserPurchaseSubscription(map);
        call.enqueue(new Callback<SuccessResGetMyPurchasedPlan>() {
            @Override
            public void onResponse(Call<SuccessResGetMyPurchasedPlan> call, Response<SuccessResGetMyPurchasedPlan> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetMyPurchasedPlan data = response.body();
                    if (data.status.equalsIgnoreCase("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        purchasedPlan.clear();
                        purchasedPlan.addAll(data.getResult());
                        binding.rvPurchasedPlan.setHasFixedSize(true);
                        binding.rvPurchasedPlan.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvPurchasedPlan.setAdapter(new MyPurchasedSubscriptionAdapter(getActivity(),purchasedPlan,MyPurchasedSubscriptionFragment.this::addFollow));
                    } else if (data.status.equalsIgnoreCase("0")) {
                        showToast(getActivity(), data.message);
                        purchasedPlan.clear();
                        binding.rvPurchasedPlan.setHasFixedSize(true);
                        binding.rvPurchasedPlan.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvPurchasedPlan.setAdapter(new MyPurchasedSubscriptionAdapter(getActivity(),purchasedPlan,MyPurchasedSubscriptionFragment.this::addFollow));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetMyPurchasedPlan> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
                Log.d(TAG, "onFailure: "+t);
            }
        });
    }
    @Override
    public void addFollow(String othersId) {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("id",othersId);
        map.put("subscriber_id",userId);
        map.put("status","Deactive");
        Call<SuccessResCancelSubscription> call = apiInterface.cancelSubscription(map);
        call.enqueue(new Callback<SuccessResCancelSubscription>() {
            @Override
            public void onResponse(Call<SuccessResCancelSubscription> call, Response<SuccessResCancelSubscription> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResCancelSubscription data = response.body();
                    if (data.status.equalsIgnoreCase("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        showToast(getActivity(), data.message);
                        getubscriptionPlan();
                    } else if (data.status.equalsIgnoreCase("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    getubscriptionPlan();
                }
            }
            @Override
            public void onFailure(Call<SuccessResCancelSubscription> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
                getubscriptionPlan();
                Log.d(TAG, "onFailure: "+t);
            }
        });
    }
}