package com.technorizen.stanrz.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.adapters.SuperlikePlanAdapter;
import com.technorizen.stanrz.databinding.FragmentPurchaseSuperlikesBinding;
import com.technorizen.stanrz.models.SuccessResGetSuperLikePlan;
import com.technorizen.stanrz.models.SuccessResProfileData;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.NetworkAvailablity;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;
import com.technorizen.stanrz.utility.SuperlikeClick;

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
 * Use the {@link PurchaseSuperlikesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PurchaseSuperlikesFragment extends Fragment implements SuperlikeClick {

    private StanrzInterface apiInterface ;

    FragmentPurchaseSuperlikesBinding binding;

    private int selectedPosition = -1;

    private boolean isSelected ;

//     map.put("plan_id",userId);
//        map.put("superlike",userId);
//        map.put("price",userId);
//        map.put("request_id",userId);
//        map.put("token",userId);
//        map.put("currency",userId);

    private ArrayList<SuccessResGetSuperLikePlan.Result> superLikeList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PurchaseSuperlikesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PurchaseSuperlikesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PurchaseSuperlikesFragment newInstance(String param1, String param2) {
        PurchaseSuperlikesFragment fragment = new PurchaseSuperlikesFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_purchase_superlikes, container, false);

        apiInterface = ApiClient.getClient().create(StanrzInterface.class);

        binding.header.imgHeader.setOnClickListener(v -> getActivity().onBackPressed());
        binding.header.tvHeader.setText("Purchase Super Likes");

        isSelected = false;

        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {

            getSuperlikePlans();

        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

        binding.btnPurchase.setOnClickListener(v ->
                {

                    if(isSelected)
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("from","PurchaseSubscription");
                        bundle.putString("plan_id",superLikeList.get(selectedPosition).getId());
                        bundle.putString("superlike",superLikeList.get(selectedPosition).getSuperlike());
                        bundle.putString("price",superLikeList.get(selectedPosition).getPrice());
                        Navigation.findNavController(v).navigate(R.id.action_purchaseSuperlikesFragment_to_addCardFragment,bundle);

                    }else
                    {
                        Toast.makeText(getActivity(),"Please select a plan",Toast.LENGTH_SHORT).show();
                    }

                }
                );

        return binding.getRoot();
    }

    public void getSuperlikePlans()
    {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetSuperLikePlan> call = apiInterface.getSuperLikesPlan(map);

        call.enqueue(new Callback<SuccessResGetSuperLikePlan>() {
            @Override
            public void onResponse(Call<SuccessResGetSuperLikePlan> call, Response<SuccessResGetSuperLikePlan> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetSuperLikePlan data = response.body();

                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        superLikeList.clear();

                        superLikeList.addAll(data.getResult());

                        binding.rvSuperlikePlan.setHasFixedSize(true);
                        binding.rvSuperlikePlan.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvSuperlikePlan.setAdapter(new SuperlikePlanAdapter(getActivity(),superLikeList,PurchaseSuperlikesFragment.this));

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetSuperLikePlan> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public void superLikeClick(int position, boolean isChecked) {

        isSelected = true;
        selectedPosition = position;

    }

}