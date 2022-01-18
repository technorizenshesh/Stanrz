package com.technorizen.stanrz.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.activites.LoginActivity;
import com.technorizen.stanrz.databinding.FragmentEditSingleSubscriptionBinding;
import com.technorizen.stanrz.models.SuccessResAddLike;
import com.technorizen.stanrz.models.SuccessResGetPackages;
import com.technorizen.stanrz.models.SuccessResUpdateVipMembership;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;

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
 * Use the {@link EditSingleSubscriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditSingleSubscriptionFragment extends Fragment {

    String rate = "";
    String month = "";
    String id="";

    private String strSuperlikes ="",strMonths = "";

    private StanrzInterface apiInterface;

    String myMonth = "";
    String myRate = "";

    FragmentEditSingleSubscriptionBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditSingleSubscriptionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditSingleSubscriptionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditSingleSubscriptionFragment newInstance(String param1, String param2) {
        EditSingleSubscriptionFragment fragment = new EditSingleSubscriptionFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_edit_single_subscription, container, false);

        binding.header.imgHeader.setOnClickListener(v -> getActivity().onBackPressed());

        apiInterface = ApiClient.getClient().create(StanrzInterface.class);

        Bundle bundle = this.getArguments();

        if(bundle!=null)
        {
            id = bundle.getString("id");
            rate = bundle.getString("rate");
            month = bundle.getString("month");
        }

        setPackage();

        binding.btnDelete.setOnClickListener(v ->
                {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Delete Plan")
                            .setMessage("Are you sure you want to delete Plan?")

                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation

                                    deletePlan();

                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                );

        binding.btnSave.setOnClickListener(v ->
                {

                    myMonth = binding.etMonth.getText().toString();

                    myRate = binding.etRate.getText().toString();

                    if(isValid())
                    {

                        editMemberShip();

                    }else
                    {
                        Toast.makeText(getActivity(), getResources().getString(R.string.on_error), Toast.LENGTH_SHORT).show();
                    }

                }
                );

        binding.header.tvHeader.setText(R.string.manage_fan_membership);

        return binding.getRoot();
    }

    public void setPackage()
    {

        binding.tvMoney.setText(rate);
        binding.tvMonth.setText("/ "+month+" Months");

    }

    public void editMemberShip()
    {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        Map<String,String> map = new HashMap<>();
        map.put("id",id);
        map.put("for_month",myMonth);
        map.put("superlike",myRate);

        Call<SuccessResUpdateVipMembership> call = apiInterface.editVipMembership(map);
        call.enqueue(new Callback<SuccessResUpdateVipMembership>() {
            @Override
            public void onResponse(Call<SuccessResUpdateVipMembership> call, Response<SuccessResUpdateVipMembership> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResUpdateVipMembership data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {

                        String dataResponse = new Gson().toJson(response.body());

                        binding.tvMoney.setText(myRate);
                        binding.tvMonth.setText("/ "+myMonth+" Months");

                        binding.etMonth.setText("");
                        binding.etRate.setText("");

                        binding.etMonth.clearFocus();
                        binding.etRate.clearFocus();

                    } else if (data.status.equals("0")) {

                        showToast(getActivity(), data.message);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResUpdateVipMembership> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private boolean isValid() {
        if (myRate.equalsIgnoreCase("")) {
            binding.etRate.setError(getString(R.string.enter_rate));
            return false;
        } else if (myMonth.equalsIgnoreCase("")) {
            binding.etMonth.setError(getString(R.string.enter_month));
            return false;
        }
        return true;
    }

    public void deletePlan()
    {

        String myUserId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",myUserId);
        map.put("id",id);

        Call<SuccessResAddLike> call = apiInterface.deleteSubscriptionPlan(map);

        call.enqueue(new Callback<SuccessResAddLike>() {
            @Override
            public void onResponse(Call<SuccessResAddLike> call, Response<SuccessResAddLike> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResAddLike data = response.body();
                    if (data.status.equalsIgnoreCase("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        getActivity().onBackPressed();

                    } else if (data.status.equalsIgnoreCase("0")) {
                        showToast(getActivity(), data.result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResAddLike> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
                Log.d(TAG, "onFailure: "+t);
            }
        });

    }

}