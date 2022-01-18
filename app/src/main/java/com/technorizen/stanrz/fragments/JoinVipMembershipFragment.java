package com.technorizen.stanrz.fragments;

import android.content.Intent;
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
import com.technorizen.stanrz.activites.HomeActivity;
import com.technorizen.stanrz.adapters.HistoryAdapter;
import com.technorizen.stanrz.adapters.SubscriptionPlanAdapter;
import com.technorizen.stanrz.adapters.SuperlikePlanAdapter;
import com.technorizen.stanrz.databinding.FragmentJoinVipMembershipBinding;
import com.technorizen.stanrz.models.SuccessResAddSubscription;
import com.technorizen.stanrz.models.SuccessResGetPackages;
import com.technorizen.stanrz.models.SuccessResPurchasePlan;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.NetworkAvailablity;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;
import com.technorizen.stanrz.utility.SubscriptionClick;

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
 * Use the {@link JoinVipMembershipFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JoinVipMembershipFragment extends Fragment implements SubscriptionClick {

    FragmentJoinVipMembershipBinding binding;
    private StanrzInterface apiInterface;
    String id = "";
    private ArrayList<SuccessResGetPackages.Result> packagesList = new ArrayList<>();
    private SuccessResAddSubscription.Result description;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public JoinVipMembershipFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JoinVipMembershipFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static JoinVipMembershipFragment newInstance(String param1, String param2) {
        JoinVipMembershipFragment fragment = new JoinVipMembershipFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_join_vip_membership, container, false);

        binding.header.imgHeader.setOnClickListener(v -> getActivity().onBackPressed());

        binding.header.tvHeader.setText(R.string.join);

        Bundle bundle = this.getArguments();

        if(bundle!=null)
        {
            id = bundle.getString("id");
        }

        apiInterface = ApiClient.getClient().create(StanrzInterface.class);

        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {
            getDescription();
            getMembership();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

        return binding.getRoot();
    }

    public void getMembership()
    {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",id);

        Call<SuccessResGetPackages> call = apiInterface.getVipMemberShip(map);
        call.enqueue(new Callback<SuccessResGetPackages>() {
            @Override
            public void onResponse(Call<SuccessResGetPackages> call, Response<SuccessResGetPackages> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetPackages data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {

                        String dataResponse = new Gson().toJson(response.body());
                        packagesList.clear();
                        packagesList.addAll(data.getResult());
                        binding.rvJoinMembership.setHasFixedSize(true);
                        binding.rvJoinMembership.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvJoinMembership.setAdapter(new SubscriptionPlanAdapter(getActivity(),packagesList,JoinVipMembershipFragment.this::superLikeClick,false));

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetPackages> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void joinMembership(String subscriberId,String planId,String superlike,String forMonths)
    {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        Map<String,String> map = new HashMap<>();
        map.put("user_id",id);
        map.put("subscriber_id",userId);
        map.put("plan_id",planId);
        map.put("superlike",superlike);
        map.put("for_month",forMonths);

        Call<SuccessResPurchasePlan> call = apiInterface.joinVipMembership(map);
        call.enqueue(new Callback<SuccessResPurchasePlan>() {
            @Override
            public void onResponse(Call<SuccessResPurchasePlan> call, Response<SuccessResPurchasePlan> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResPurchasePlan data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        showToast(getActivity(), data.message);
                        String dataResponse = new Gson().toJson(response.body());
                        getMembership();
                        startActivity(new Intent(getActivity(), HomeActivity.class));
                        getActivity().finish();
                    } else if (data.status.equals("0")) {

                        showToast(getActivity(), data.message);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResPurchasePlan> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void getDescription()
    {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        Map<String,String> map = new HashMap<>();
        map.put("user_id",id);

        Call<SuccessResAddSubscription> call = apiInterface.getDescription(map);
        call.enqueue(new Callback<SuccessResAddSubscription>() {
            @Override
            public void onResponse(Call<SuccessResAddSubscription> call, Response<SuccessResAddSubscription> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAddSubscription data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {

                        String dataResponse = new Gson().toJson(response.body());
                        showToast(getActivity(), data.message);

                        description = data.getResult();

                        binding.tvDescription.setText(description.getDescription());

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResAddSubscription> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    @Override
    public void superLikeClick(View view, int position, String params2) {

        joinMembership("",packagesList.get(position).getId(),packagesList.get(position).getSuperlike(),packagesList.get(position).getForMonth());

    }
}