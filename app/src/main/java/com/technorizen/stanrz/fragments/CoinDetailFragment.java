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
import com.technorizen.stanrz.adapters.HistoryAdapter;
import com.technorizen.stanrz.databinding.FragmentCoinDetailBinding;
import com.technorizen.stanrz.models.SuccessResAddLike;
import com.technorizen.stanrz.models.SuccessResExchangeCoinstoSuperlike;
import com.technorizen.stanrz.models.SuccessResGetTransactionHistory;
import com.technorizen.stanrz.models.SuccessResProfileData;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.NetworkAvailablity;
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
 * Use the {@link CoinDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoinDetailFragment extends Fragment {

    private ArrayList<SuccessResGetTransactionHistory.Result> transactionHistory = new ArrayList<>();

    private SuccessResProfileData.Result userDetail;

    FragmentCoinDetailBinding binding;

    private StanrzInterface apiInterface;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CoinDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CoinDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CoinDetailFragment newInstance(String param1, String param2) {
        CoinDetailFragment fragment = new CoinDetailFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_coin_detail, container, false);

        apiInterface = ApiClient.getClient().create(StanrzInterface.class);

        binding.header.imgHeader.setOnClickListener(v -> getActivity().onBackPressed());
        binding.header.tvHeader.setText(R.string.stanrz_wallet);

        setAllClick();

        binding.btnExchange.setOnClickListener(v ->
                {

                    if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {

                        if(!binding.etEnterCoins.getText().toString().equalsIgnoreCase(""))
                        {
                            exchangeCoinsToSuperLike(binding.etEnterCoins.getText().toString());
                        }
                        else
                        {
                            Toast.makeText(getActivity(), ""+R.string.enter_coins_to,Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                    }
                }
                );

        binding.btnReddemCoins.setOnClickListener(v ->
                {

                    if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {

                        if(!binding.etRadeemCoins.getText().toString().equalsIgnoreCase(""))
                        {
                            int coins = Integer.parseInt(binding.etRadeemCoins.getText().toString());

                            if(coins>=100)
                            {
                                exchangeSuperLikesToCoins(binding.etRadeemCoins.getText().toString());
                            }
                            else {

                                Toast.makeText(getActivity(),""+ R.string.must_greater_than,Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getActivity(),""+ R.string.enter_coins,Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {
            getHistory();
            getProfile();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }
        return binding.getRoot();
    }

    public void setAllClick()
    {
        binding.cvHistory.setOnClickListener(v ->
                {
                    getHistory();
                    binding.llHistory.setVisibility(View.VISIBLE);
                    binding.rlExcahngeSuperlikes.setVisibility(View.GONE);
                    binding.rlWithdrawCash.setVisibility(View.GONE);
                    binding.rlWithdrawCoins.setVisibility(View.GONE);
                }
                );

        binding.cvExcahngeCoins.setOnClickListener(v ->
                {
                    binding.llHistory.setVisibility(View.GONE);
                    binding.rlExcahngeSuperlikes.setVisibility(View.VISIBLE);
                    binding.rlWithdrawCash.setVisibility(View.GONE);
                    binding.rlWithdrawCoins.setVisibility(View.GONE);
                }
        );

        binding.cvWithdrawCoins.setOnClickListener(v ->
                {
                    binding.llHistory.setVisibility(View.GONE);
                    binding.rlExcahngeSuperlikes.setVisibility(View.GONE);
                    binding.rlWithdrawCash.setVisibility(View.GONE);
                    binding.rlWithdrawCoins.setVisibility(View.VISIBLE);
                }
        );

        binding.cvWithdrawCash.setOnClickListener(v ->
                {
                    binding.llHistory.setVisibility(View.GONE);
                    binding.rlExcahngeSuperlikes.setVisibility(View.GONE);
                    binding.rlWithdrawCash.setVisibility(View.VISIBLE);
                    binding.rlWithdrawCoins.setVisibility(View.GONE);
                }
        );
    }

    public void exchangeCoinsToSuperLike(String coins)
    {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("coins",coins);

        Call<SuccessResExchangeCoinstoSuperlike> call = apiInterface.exchangeCoinsToSuperLike(map);

        call.enqueue(new Callback<SuccessResExchangeCoinstoSuperlike>() {
            @Override
            public void onResponse(Call<SuccessResExchangeCoinstoSuperlike> call, Response<SuccessResExchangeCoinstoSuperlike> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResExchangeCoinstoSuperlike data = response.body();
                    Log.e("data",data.status);
                    binding.etEnterCoins.setText("");
                    getProfile();
                    showToast(getActivity(), data.message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResExchangeCoinstoSuperlike> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void exchangeSuperLikesToCoins(String coins)
    {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("coins",coins);
        Call<SuccessResExchangeCoinstoSuperlike> call = apiInterface.exchangeCoinsToCash(map);
        call.enqueue(new Callback<SuccessResExchangeCoinstoSuperlike>() {
            @Override
            public void onResponse(Call<SuccessResExchangeCoinstoSuperlike> call, Response<SuccessResExchangeCoinstoSuperlike> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResExchangeCoinstoSuperlike data = response.body();
                    Log.e("data",data.status);
                    showToast(getActivity(), data.message);
                    binding.etRadeemCoins.setText("");
                    getProfile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResExchangeCoinstoSuperlike> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

    public void getHistory()
    {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetTransactionHistory> call = apiInterface.getTransactionHistory(map);

        call.enqueue(new Callback<SuccessResGetTransactionHistory>() {
            @Override
            public void onResponse(Call<SuccessResGetTransactionHistory> call, Response<SuccessResGetTransactionHistory> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetTransactionHistory data = response.body();
                    Log.e("data",data.status);

                    if(data.status.equalsIgnoreCase("1"))
                    {
                        transactionHistory.clear();
                        transactionHistory.addAll(data.getResult());
                        binding.rvHistory.setHasFixedSize(true);
                        binding.rvHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvHistory.setAdapter(new HistoryAdapter(getActivity(),transactionHistory));
                    }else
                    {
                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetTransactionHistory> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void getProfile() {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResProfileData> call = apiInterface.getProfile(map);

        call.enqueue(new Callback<SuccessResProfileData>() {
            @Override
            public void onResponse(Call<SuccessResProfileData> call, Response<SuccessResProfileData> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResProfileData data = response.body();
                    userDetail = data.getResult();
//                    setSellerData();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        binding.tvTotalCoins.setText(userDetail.getTotalPCoins());
                        binding.tvTotalEarning.setText(getString(R.string.total_earning)+" "+userDetail.getWallet());

//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResProfileData> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


}