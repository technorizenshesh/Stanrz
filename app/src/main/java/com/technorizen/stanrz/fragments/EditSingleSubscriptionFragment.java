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
import com.technorizen.stanrz.models.SuccessResHideShowPlan;
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
    private String strSuperlikes ="",strMonths = "",strStatus="";
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_edit_single_subscription, container, false);
        binding.header.imgHeader.setOnClickListener(v -> getActivity().onBackPressed());
        apiInterface = ApiClient.getClient().create(StanrzInterface.class);
        Bundle bundle = this.getArguments();
        if(bundle!=null)
        {
            id = bundle.getString("id");
            rate = bundle.getString("rate");
            month = bundle.getString("month");
            strStatus = bundle.getString("status");
        }
        setPackage();
        binding.btnDelete.setOnClickListener(v ->
                {
                    if(month.equalsIgnoreCase("Monthly"))
                    {
                        new AlertDialog.Builder(getActivity())
                                .setTitle(R.string.delete_plan)
                                .setMessage(R.string.delete_plans)
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
                    else
                    {
                        new AlertDialog.Builder(getActivity())
                                .setTitle(R.string.delete_plan)
                                .setMessage(R.string.are_yousure)
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
                }
                );
        binding.tvHideShow.setOnClickListener(v ->
                {
                    hideShowPlan();
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

    private void hideShowPlan()
    {
        if(strStatus.equalsIgnoreCase(getString(R.string.hide)))
        {
            strStatus = getString(R.string.show);
            binding.tvHideShow.setText(R.string.tap_to_hide);
            binding.tvHideShowDesc.setText(getString(R.string.text_hide));
            binding.ll25.setBackgroundResource(R.drawable.ic_blank_blue);
            binding.tvEdit.setBackgroundResource(R.drawable.blue_button_bg);
        }
        else
        {
            strStatus = getString(R.string.hide);
            binding.tvHideShow.setText(R.string.tap_to_show);
            binding.tvHideShowDesc.setText(getString(R.string.text_show));
            binding.ll25.setBackgroundResource(R.drawable.ic_blank_black);
            binding.tvEdit.setBackgroundResource(R.drawable.black_button_bg);
        }

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("id",id);
        map.put("view_status",strStatus);
        Call<SuccessResHideShowPlan> call = apiInterface.hideSHowMonthlyPlan(map);
        call.enqueue(new Callback<SuccessResHideShowPlan>() {
            @Override
            public void onResponse(Call<SuccessResHideShowPlan> call, Response<SuccessResHideShowPlan> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResHideShowPlan data = response.body();
                    Log.e("data",data.status);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResHideShowPlan> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void setPackage()
    {
        binding.tvMoney.setText(rate);
        if(month.equalsIgnoreCase("Monthly"))
        {
            binding.tvMonth.setText("/ "+month);
            binding.etMonth.setVisibility(View.GONE);
            binding.etRate.setVisibility(View.GONE);
            binding.btnSave.setVisibility(View.GONE);
            binding.etMonth.setText(R.string.monthly);
            binding.tvHideShow.setVisibility(View.VISIBLE);
            binding.tvHideShowDesc.setVisibility(View.VISIBLE);
            if(strStatus.equalsIgnoreCase(getString(R.string.hide)))
            {
                binding.tvHideShow.setText(R.string.tap_to_show);
                binding.ll25.setBackgroundResource(R.drawable.ic_blank_black);
                binding.tvEdit.setBackgroundResource(R.drawable.black_button_bg);
                binding.tvHideShowDesc.setText(getString(R.string.text_show));
            }
            else
            {
                binding.tvHideShow.setText(R.string.tap_to_hide);
                binding.tvHideShowDesc.setText(getString(R.string.text_hide));
                binding.ll25.setBackgroundResource(R.drawable.ic_blank_blue);
                binding.tvEdit.setBackgroundResource(R.drawable.blue_button_bg);
            }
        }
        else
        {
            binding.tvMonth.setText("/ "+month+" Months");
            binding.etMonth.setVisibility(View.VISIBLE);
        }
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
                        if(month.equalsIgnoreCase("Monthly"))
                        {
                            binding.tvMonth.setText("/ "+month);
                        }
                        else
                        {
                            binding.tvMonth.setText("/ "+month+" Months");
                            binding.etMonth.setText("");
                        }
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