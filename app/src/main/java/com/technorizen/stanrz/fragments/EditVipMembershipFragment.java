package com.technorizen.stanrz.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.telephony.SubscriptionPlan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.view.CardForm;
import com.google.gson.Gson;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.activites.LoginActivity;
import com.technorizen.stanrz.adapters.SubscriptionPlanAdapter;

import com.technorizen.stanrz.databinding.FragmentEditVipMembershipBinding;
import com.technorizen.stanrz.models.SuccessResAddSubscription;
import com.technorizen.stanrz.models.SuccessResAddSuperLike;
import com.technorizen.stanrz.models.SuccessResGetChat;
import com.technorizen.stanrz.models.SuccessResGetPackages;
import com.technorizen.stanrz.models.SuccessResProfileData;
import com.technorizen.stanrz.models.SuccessResUpdateVipMembership;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;
import com.technorizen.stanrz.utility.SubscriptionClick;
import com.technorizen.stanrz.utility.SuperlikeClick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technorizen.stanrz.activites.LoginActivity.TAG;
import static com.technorizen.stanrz.retrofit.Constant.USER_ID;
import static com.technorizen.stanrz.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditVipMembershipFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditVipMembershipFragment extends Fragment implements SubscriptionClick {

    FragmentEditVipMembershipBinding binding;

    private SuccessResAddSubscription.Result description;

    private String fanClubSubscription = "";

    Dialog dialog;

    private String strSuperlikes ="",strMonths = "";

    private StanrzInterface apiInterface;

    private ArrayList<SuccessResGetPackages.Result> packagesList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditVipMembershipFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EditVipMembershipFragment newInstance(String param1, String param2) {
        EditVipMembershipFragment fragment = new EditVipMembershipFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_edit_vip_membership, container, false);

        apiInterface = ApiClient.getClient().create(StanrzInterface.class);

        binding.header.tvHeader.setText(R.string.edit_vip_mem);

        getProfile();

        getDescription();

        getMembership();

        binding.btnCreateSubscription.setOnClickListener(v ->
                {
                    fullScreenDialog();
                }
                );

        binding.checkboxEnableDisable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    updateFanClubMembership("1");
                    binding.llSubscription.setVisibility(View.VISIBLE);
                }
                else
                {
                    updateFanClubMembership("0");
                    binding.llSubscription.setVisibility(View.GONE);
                }
            }
        });

        binding.btnApplyForPlan.setOnClickListener(view ->
                {

                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.fan_club)
                            .setMessage(R.string.are_you_sure)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    verifyForOpenFanClub();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                );

        binding.header.imgHeader.setOnClickListener(v ->
                {
                    getActivity().onBackPressed();
                }
                );

        binding.btnAdd.setOnClickListener(view ->
                {
                    if(!binding.etDescription.getText().toString().equalsIgnoreCase(""))
                    {
                        addDescription(binding.etDescription.getText().toString());
                    }else
                    {
                        showToast(getActivity(),""+getActivity().getString(R.string.enter_description));
                    }
                }
                );
        return binding.getRoot();
    }

    private void fullScreenDialog() {
        dialog = new Dialog(getActivity(), WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.dialog_add_subscrption_plan);
        AppCompatButton btnAdd =  dialog.findViewById(R.id.btnAdd);
        CheckBox checkBox = dialog.findViewById(R.id.checkBoxMonthly);
        ImageView ivBack;
        EditText etSuperlies,etMonth;
        etSuperlies = dialog.findViewById(R.id.etRate);
        etMonth = dialog.findViewById(R.id.etMonth);
        ivBack = dialog.findViewById(R.id.img_header);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    etMonth.setEnabled(false);
                    etMonth.setText(R.string.monthly);
                }
                else
                {
                    etMonth.setEnabled(true);
                    etMonth.setText("");
                }
            }
        });

        ivBack.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );

        btnAdd.setOnClickListener(v ->
                {
                    strSuperlikes = etSuperlies.getText().toString();
                    strMonths = etMonth.getText().toString();
                    if(checkBox.isChecked())
                    {
                        if(isValid1())
                        {
                            addSubcriptionPlan();
                        }
                    }
                    else
                    {
                        if(!strMonths.equalsIgnoreCase(""))
                        {
                            if(isValid(Integer.parseInt(strMonths)))
                            {
                                addSubcriptionPlan();
                            }else
                            {
                                Toast.makeText(getActivity(),getActivity().getString(R.string.on_error)+"",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            showToast(getActivity(),getString(R.string.enter_months));
                        }
                    }
                }
                );
        dialog.show();
    }

    public boolean isValid(int months)
    {
        if(strMonths.equalsIgnoreCase(""))
        {
            showToast(getActivity(),getString(R.string.enter_months));
            return false;
        }else if(months < 1 && months > 12)
        {
            showToast(getActivity(),getString(R.string.enter_valid_months));
            return false;
        } else if(strSuperlikes.equalsIgnoreCase(""))
        {
            showToast(getActivity(),getString(R.string.enter_superlikes));
            return false;
        }
        return true;
    }

    public boolean isValid1()
    {
        if(strSuperlikes.equalsIgnoreCase(""))
        {
            showToast(getActivity(),getString(R.string.enter_superlikes));
            return false;
        }
        return true;
    }

    public void addSubcriptionPlan()
    {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("for_month",strMonths);
//        map.put("formonth",strForMonthly);
        map.put("superlike",strSuperlikes);

        Call<SuccessResAddSuperLike> call = apiInterface.addSubscriptionPlan(map);

        call.enqueue(new Callback<SuccessResAddSuperLike>() {
            @Override
            public void onResponse(Call<SuccessResAddSuperLike> call, Response<SuccessResAddSuperLike> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResAddSuperLike data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        showToast(getActivity(), data.message);
                        dialog.dismiss();
                        getMembership();
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResAddSuperLike> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }
    public void updateFanClubMembership(String status)
    {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("fan_club",status);

        Call<SuccessResUpdateVipMembership> call = apiInterface.updateFanClub(map);

        call.enqueue(new Callback<SuccessResUpdateVipMembership>() {
            @Override
            public void onResponse(Call<SuccessResUpdateVipMembership> call, Response<SuccessResUpdateVipMembership> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResUpdateVipMembership data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        showToast(getActivity(), data.message);

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

    private SuccessResProfileData.Result userDetails;

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
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        fanClubSubscription = data.getResult().getFanClub();
                        userDetails = data.getResult();
                        setFanClubSubscription();

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

    public void setFanClubSubscription()
    {

        if(userDetails.getOpenFanClub().equalsIgnoreCase("approvedbyadmin"))
        {
            binding.layoutApply.setVisibility(View.VISIBLE);
        } else if(userDetails.getOpenFanClub().equalsIgnoreCase("done"))
        {
            binding.layoutApply.setVisibility(View.GONE);
            binding.llDone.setVisibility(View.VISIBLE);
        }else if(userDetails.getOpenFanClub().equalsIgnoreCase("Pending"))
        {
            binding.layoutApply.setVisibility(View.VISIBLE);
            binding.llDone.setVisibility(View.GONE);
            binding.tvVerifyAccount.setVisibility(View.GONE);
            binding.tvPendingVerification.setVisibility(View.VISIBLE);
            binding.btnApplyForPlan.setVisibility(View.GONE);
        }else if(userDetails.getOpenFanClub().equalsIgnoreCase(""))
        {
            binding.layoutApply.setVisibility(View.VISIBLE);
            binding.llDone.setVisibility(View.GONE);
            binding.tvVerifyAccount.setVisibility(View.GONE);
            binding.tvPendingVerification.setVisibility(View.GONE);
            binding.btnApplyForPlan.setVisibility(View.GONE);
            binding.tvVerficationLink.setVisibility(View.VISIBLE);
            binding.tvVerficationAccountLink.setVisibility(View.VISIBLE);
        }

        binding.tvVerficationLink.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_editVipMembershipFragment_to_applyForFanAccountFragment);
                }
                );

            if(fanClubSubscription.equalsIgnoreCase("0"))
        {
            binding.checkboxEnableDisable.setChecked(false);
        }
        else
        {
            binding.checkboxEnableDisable.setChecked(true);
            binding.llSubscription.setVisibility(View.VISIBLE);
        }
    }

    private void verifyForOpenFanClub() {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("OpenFanClub","done");

        Call<SuccessResProfileData> call = apiInterface.approvedForFanPlan(map);

        call.enqueue(new Callback<SuccessResProfileData>() {
            @Override
            public void onResponse(Call<SuccessResProfileData> call, Response<SuccessResProfileData> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResProfileData data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        fanClubSubscription = data.getResult().getFanClub();
                        userDetails = data.getResult();
                        setFanClubSubscription();

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

    public void getMembership()
    {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("type","Mine");

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

                        binding.rvSubcriptionItems.setHasFixedSize(true);
                        binding.rvSubcriptionItems.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvSubcriptionItems.setAdapter(new SubscriptionPlanAdapter(getActivity(),packagesList,EditVipMembershipFragment.this::superLikeClick,true));

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

    @Override
    public void superLikeClick(View v, int position, String params2) {
        Bundle bundle = new Bundle();
        bundle.putString("rate",packagesList.get(position).getSuperlike());
        bundle.putString("month",packagesList.get(position).getForMonth());
        bundle.putString("id",packagesList.get(position).getId());
        bundle.putString("status",packagesList.get(position).getViewStatus());
        Navigation.findNavController(v).navigate(R.id.action_editVipMembershipFragment_to_editSingleSubscriptionFragment,bundle);

    }

    public void addDescription(String desc)
    {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("description",desc);

        Call<SuccessResAddSubscription> call = apiInterface.addDescription(map);
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

    public void getDescription()
    {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);

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

                        binding.etDescription.setText(description.getDescription());

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

}