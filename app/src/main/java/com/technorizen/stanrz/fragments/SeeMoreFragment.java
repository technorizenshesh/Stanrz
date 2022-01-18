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
import com.technorizen.stanrz.adapters.IamStanAdapter;
import com.technorizen.stanrz.adapters.StanrzAdapter;
import com.technorizen.stanrz.adapters.TopAllStanrzAdapter;
import com.technorizen.stanrz.databinding.FragmentSeeMoreBinding;
import com.technorizen.stanrz.databinding.TopStanrzItemBinding;
import com.technorizen.stanrz.models.SuccessResGetStanrzOf;
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
 * Use the {@link SeeMoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeeMoreFragment extends Fragment {

    FragmentSeeMoreBinding binding;

    private String userId="";
    private ArrayList<SuccessResGetStanrzOf.Result> topStanrzList = new ArrayList<>();

    private String type = "dailytop";

    private StanrzInterface apiInterface;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SeeMoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SeeMoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SeeMoreFragment newInstance(String param1, String param2) {
        SeeMoreFragment fragment = new SeeMoreFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_see_more, container, false);

        apiInterface = ApiClient.getClient().create(StanrzInterface.class);

        Bundle bundle = this.getArguments();

        if (bundle!=null)
        {
            userId = bundle.getString("id");

            if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {

                getTopStanrz();

            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
            }

            allClick();

        }

        binding.header.imgHeader.setOnClickListener(v -> getActivity().onBackPressed());

        binding.header.tvHeader.setText(R.string.top_stanrz);

        return binding.getRoot();
    }

    public void allClick()
    {

        binding.cvDaily.setOnClickListener(v ->
                {

                    type = "dailytop";

                    getTopStanrz();

                    binding.tvDaily.setBackgroundResource(R.drawable.ic_header_bg);
                    binding.tvDaily.setTextColor(getResources().getColor(R.color.white));

                    binding.tvWeekly.setBackgroundResource(0);
                    binding.tvWeekly.setTextColor(getResources().getColor(R.color.black));

                    binding.tvMontly.setBackgroundResource(0);
                    binding.tvMontly.setTextColor(getResources().getColor(R.color.black));

                    binding.tvAllTime.setBackgroundResource(0);
                    binding.tvAllTime.setTextColor(getResources().getColor(R.color.black));

                }
        );

        binding.cvWeekly.setOnClickListener(v ->
                {

                    type = "weekly";

                    getTopStanrz();

                    binding.tvWeekly.setBackgroundResource(R.drawable.ic_header_bg);
                    binding.tvWeekly.setTextColor(getResources().getColor(R.color.white));

                    binding.tvDaily.setBackgroundResource(0);
                    binding.tvDaily.setTextColor(getResources().getColor(R.color.black));

                    binding.tvMontly.setBackgroundResource(0);
                    binding.tvMontly.setTextColor(getResources().getColor(R.color.black));

                    binding.tvAllTime.setBackgroundResource(0);
                    binding.tvAllTime.setTextColor(getResources().getColor(R.color.black));

                }
        );

        binding.cvMonthly.setOnClickListener(v ->
                {

                    type = "monthly";
                    getTopStanrz();

                    binding.tvMontly.setBackgroundResource(R.drawable.ic_header_bg);
                    binding.tvMontly.setTextColor(getResources().getColor(R.color.white));

                    binding.tvDaily.setBackgroundResource(0);
                    binding.tvDaily.setTextColor(getResources().getColor(R.color.black));

                    binding.tvWeekly.setBackgroundResource(0);
                    binding.tvWeekly.setTextColor(getResources().getColor(R.color.black));

                    binding.tvAllTime.setBackgroundResource(0);
                    binding.tvAllTime.setTextColor(getResources().getColor(R.color.black));

                }
        );

        binding.cvAllTime.setOnClickListener(v ->
                {
                    type = "alltime";

                    getTopStanrz();

                    binding.tvAllTime.setBackgroundResource(R.drawable.ic_header_bg);
                    binding.tvAllTime.setTextColor(getResources().getColor(R.color.white));

                    binding.tvDaily.setBackgroundResource(0);
                    binding.tvDaily.setTextColor(getResources().getColor(R.color.black));

                    binding.tvWeekly.setBackgroundResource(0);
                    binding.tvWeekly.setTextColor(getResources().getColor(R.color.black));

                    binding.tvMontly.setBackgroundResource(0);
                    binding.tvMontly.setTextColor(getResources().getColor(R.color.black));

                }
        );
    }

    private void getTopStanrz() {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("type",type);

        Call<SuccessResGetStanrzOf> call = apiInterface.getTopStanrz(map);

        call.enqueue(new Callback<SuccessResGetStanrzOf>() {
            @Override
            public void onResponse(Call<SuccessResGetStanrzOf> call, Response<SuccessResGetStanrzOf> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResGetStanrzOf data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        topStanrzList.clear();
                        topStanrzList.addAll(data.getResult());
                        binding.rvStanrz.setHasFixedSize(true);
                        binding.rvStanrz.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvStanrz.setAdapter(new TopAllStanrzAdapter(getActivity(),topStanrzList));
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                        topStanrzList.clear();
                        binding.rvStanrz.setHasFixedSize(true);
                        binding.rvStanrz.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvStanrz.setAdapter(new TopAllStanrzAdapter(getActivity(),topStanrzList));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetStanrzOf> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }
}