package com.technorizen.stanrz.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.util.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.activites.SplashActivity;
import com.technorizen.stanrz.adapters.SearchAdapter;
import com.technorizen.stanrz.databinding.FragmentSearchUsersBinding;
import com.technorizen.stanrz.models.SuccessResGetUser;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.Constant;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.technorizen.stanrz.retrofit.Constant.USER_ID;
import static com.technorizen.stanrz.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchUsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchUsersFragment extends Fragment {

    FragmentSearchUsersBinding binding;
    private StanrzInterface apiInterface;

    private List<SuccessResGetUser.Result> usersList = new LinkedList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchUsersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchUsersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchUsersFragment newInstance(String param1, String param2) {
        SearchUsersFragment fragment = new SearchUsersFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_search_users, container, false);

        apiInterface = ApiClient.getClient().create(StanrzInterface.class);

        binding.ivBack.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        binding.etSearch.requestFocus();

        binding.etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

                getUsers(cs.toString());

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });

        return binding.getRoot();
    }

    private void getUsers(String title) {

        String userId=  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("title",title);

        Call<SuccessResGetUser> call = apiInterface.getUsers(map);

        call.enqueue(new Callback<SuccessResGetUser>() {
            @Override
            public void onResponse(Call<SuccessResGetUser> call, Response<SuccessResGetUser> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetUser data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        usersList.clear();

                        usersList.addAll(data.getResult());

                        binding.rvUsers.setHasFixedSize(true);
                        binding.rvUsers.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvUsers.setAdapter(new SearchAdapter(getActivity(),usersList));


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
            public void onFailure(Call<SuccessResGetUser> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        binding.etSearch.requestFocus();


    }
}