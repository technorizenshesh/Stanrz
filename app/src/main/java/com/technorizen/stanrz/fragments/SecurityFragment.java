package com.technorizen.stanrz.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.adapters.BlockedUserAdapter;
import com.technorizen.stanrz.adapters.MessageAdapter;
import com.technorizen.stanrz.databinding.FragmentSecurityBinding;
import com.technorizen.stanrz.models.SuccessResAddLike;
import com.technorizen.stanrz.models.SuccessResGetBlockedUser;
import com.technorizen.stanrz.models.SuccessResGetConversation;
import com.technorizen.stanrz.retrofit.ApiClient;
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

import static android.content.ContentValues.TAG;
import static com.technorizen.stanrz.retrofit.Constant.USER_ID;
import static com.technorizen.stanrz.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecurityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecurityFragment extends Fragment implements SubscriptionClick {

    FragmentSecurityBinding binding;

    private ArrayList<SuccessResGetBlockedUser.Result> blockedUser = new ArrayList<>();

    private StanrzInterface apiInterface;

    private BlockedUserAdapter blockedUserAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SecurityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecurityFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static SecurityFragment newInstance(String param1, String param2) {
        SecurityFragment fragment = new SecurityFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_security, container, false);
        apiInterface = ApiClient.getClient().create(StanrzInterface.class);
        binding.header.imgHeader.setOnClickListener(view ->
                {
                    getActivity().onBackPressed();
                }
                );
        binding.header.tvHeader.setText(R.string.security);

        blockedUserAdapter = new BlockedUserAdapter(getActivity(),blockedUser,SecurityFragment.this);

        binding.rvBlocked.setHasFixedSize(true);
        binding.rvBlocked.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvBlocked.setAdapter(blockedUserAdapter);

        getBlockedUser();

        return binding.getRoot();
    }

    public void getBlockedUser()
    {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetBlockedUser> call = apiInterface.getBlockedUser(map);

        call.enqueue(new Callback<SuccessResGetBlockedUser>() {
            @Override
            public void onResponse(Call<SuccessResGetBlockedUser> call, Response<SuccessResGetBlockedUser> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResGetBlockedUser data = response.body();

                    if (data.status.equalsIgnoreCase("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        blockedUser.clear();
                        blockedUser.addAll(data.getResult());
                        blockedUserAdapter.notifyDataSetChanged();

                    } else if (data.status.equalsIgnoreCase("0")) {
                        showToast(getActivity(), data.message);

                        blockedUser.clear();

                        blockedUserAdapter.notifyDataSetChanged();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetBlockedUser> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
                Log.d(TAG, "onFailure: "+t);

            }
        });
    }

    @Override
    public void superLikeClick(View view, int position, String params2) {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("block_user_id",params2);

        Call<SuccessResAddLike> call = apiInterface.blockUser(map);

        call.enqueue(new Callback<SuccessResAddLike>() {
            @Override
            public void onResponse(Call<SuccessResAddLike> call, Response<SuccessResAddLike> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAddLike data = response.body();
                    Log.e("data",data.status);
                    showToast(getActivity(), data.result);
                    getBlockedUser();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResAddLike> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }
}