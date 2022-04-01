package com.technorizen.stanrz.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.view.CardForm;
import com.google.gson.Gson;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.activites.HomeActivity;
import com.technorizen.stanrz.adapters.CardAdapter;
import com.technorizen.stanrz.adapters.SuperlikePlanAdapter;
import com.technorizen.stanrz.databinding.FragmentAddCardBinding;
import com.technorizen.stanrz.models.SuccessResAddCard;
import com.technorizen.stanrz.models.SuccessResGetCards;
import com.technorizen.stanrz.models.SuccessResGetSuperLikePlan;
import com.technorizen.stanrz.models.SuccessResGetToken;
import com.technorizen.stanrz.models.SuccessResPurchaseSuperlike;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;
import com.technorizen.stanrz.utility.UpdateAndDeleteAddress;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.technorizen.stanrz.retrofit.Constant.USER_ID;
import static com.technorizen.stanrz.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCardFragment extends Fragment implements UpdateAndDeleteAddress {

    FragmentAddCardBinding binding;

    Dialog dialog;

    StanrzInterface apiInterface;

    String from = "", strPlanId = "",strSuperLike ="",strPrice = "",token = "";

    private boolean fromWhere;

    private int selectedPosition = -1;

    private int cardSelectedPosition = -1;

    private boolean selected = false;

    private List<SuccessResGetCards.Result> cardList = new LinkedList<>();

    String cardNo ="",expirationMonth="",expirationYear="",cvv = "",holderName="";

    String myCardNumb = "",year = "",month , myCVV = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddCardFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static AddCardFragment newInstance(String param1, String param2) {
        AddCardFragment fragment = new AddCardFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_card, container, false);
        apiInterface = ApiClient.getClient().create(StanrzInterface.class);
        selected = false;
        binding.imgAdd.setOnClickListener(v ->
                {
                    fullScreenDialog();
                }
        );

        binding.header.imgHeader.setOnClickListener(v -> getActivity().onBackPressed());
        binding.header.tvHeader.setText(R.string.choose_card);
        getCards();
        Bundle bundle = this.getArguments();
        if (bundle!=null)
        {
            from = bundle.getString("from");
            if(from.equalsIgnoreCase("PurchaseSubscription"))
            {
                strPlanId = bundle.getString("plan_id");
                strSuperLike = bundle.getString("superlike");
                strPrice = bundle.getString("price");
            }
        }

        binding.btnPay.setOnClickListener(v ->
                {
                    if(selected)
                    {
                        getToken();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Please Select a Card.",Toast.LENGTH_SHORT).show();
                    }
                }
                );

        return binding.getRoot();
    }

    private void fullScreenDialog() {
        dialog = new Dialog(getActivity(), WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.dialog_add_card);
        AppCompatButton btnAdd =  dialog.findViewById(R.id.btnAdd);
        ImageView ivBack;
        ivBack = dialog.findViewById(R.id.img_header);
        CardForm cardForm = dialog.findViewById(R.id.card_form);
        cardForm.cardRequired(true)
                .maskCardNumber(true)
                .maskCvv(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .saveCardCheckBoxChecked(false)
                .saveCardCheckBoxVisible(false)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .mobileNumberExplanation("Make sure SMS is enabled for this mobile number")
                .actionLabel("Purchase")
                .setup((AppCompatActivity) getActivity());

        cardForm.setOnCardFormSubmitListener(new OnCardFormSubmitListener() {
            @Override
            public void onCardFormSubmit() {
                cardNo = cardForm.getCardNumber();
                expirationMonth = cardForm.getExpirationMonth();
                expirationYear = cardForm.getExpirationYear();
                cvv = cardForm.getCvv();
                holderName = cardForm.getCardholderName();

                if(cardForm.isValid())
                {

                    addCardDetails();

                }else
                {
                    cardForm.validate();
                }
            }
        });

        btnAdd.setOnClickListener(v ->
                {

                    cardNo = cardForm.getCardNumber();
                    expirationMonth = cardForm.getExpirationMonth();
                    expirationYear = cardForm.getExpirationYear();
                    cvv = cardForm.getCvv();
                    holderName = cardForm.getCardholderName();
                    if(cardForm.isValid())
                    {
                        addCardDetails();
                    }else
                    {
                        cardForm.validate();
                    }
                }
        );
        ivBack.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );
        dialog.show();
    }

    private void addCardDetails()
    {
        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(),getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("card_no",cardNo);
        map.put("exp_month",expirationMonth);
        map.put("exp_year",expirationYear);
        map.put("cvv",cvv);
        map.put("card_holder_name",holderName);

        Log.e(TAG,"Test Request "+map);
        Call<SuccessResAddCard> loginCall = apiInterface.addCard(map);
        loginCall.enqueue(new Callback<SuccessResAddCard>() {
            @Override
            public void onResponse(Call<SuccessResAddCard> call, Response<SuccessResAddCard> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAddCard data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    dialog.dismiss();
                    getCards();
                    Log.e(TAG,"Test Response :"+responseString);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG,"Test Response :"+response.body());
                }
            }

            @Override
            public void onFailure(Call<SuccessResAddCard> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    private void getCards()
    {

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(),getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResGetCards> call = apiInterface.getCards(map);
        call.enqueue(new Callback<SuccessResGetCards>() {
            @Override
            public void onResponse(Call<SuccessResGetCards> call, Response<SuccessResGetCards> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetCards data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        binding.btnPay.setVisibility(View.VISIBLE);
                        cardList.clear();
                        cardList.addAll(data.getResult());
                        binding.rvCards.setHasFixedSize(true);
                        binding.rvCards.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvCards.setAdapter(new CardAdapter(getActivity(),cardList,AddCardFragment.this,true));
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                        cardList.clear();
                        binding.btnPay.setVisibility(View.GONE);

                        binding.rvCards.setHasFixedSize(true);
                        binding.rvCards.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvCards.setAdapter(new CardAdapter(getActivity(),cardList,AddCardFragment.this,true));

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetCards> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

    @Override
    public void updateCart(View v, String name, String address, String id, String phone, String countrycode) {

    }

    @Override
    public void deleteCart(String productId) {

    }

    @Override
    public void isSelected(boolean isSelected,int position) {

        selected = isSelected;
        selectedPosition = position;
        cardSelectedPosition = position;

    }


    public void getToken()
    {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("card_number",cardList.get(cardSelectedPosition).getCardNo());
        map.put("expiry_year",cardList.get(cardSelectedPosition).getExpYear());
        map.put("expiry_month",cardList.get(cardSelectedPosition).getExpMonth());
        map.put("cvc_code",cardList.get(cardSelectedPosition).getCvv());

        Call<SuccessResGetToken> call = apiInterface.getToken(map);
        call.enqueue(new Callback<SuccessResGetToken>() {
            @Override
            public void onResponse(Call<SuccessResGetToken> call, Response<SuccessResGetToken> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetToken data = response.body();
                    if (data.status == 1) {


                        token = data.getResult().getId();
                        if(from.equalsIgnoreCase("PurchaseSubscription"))
                        {

                            makePaymentForSuperLike();

                        }

                    } else {
                        showToast(getActivity(), data.message);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResGetToken> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });
    }

    public void makePaymentForSuperLike()
    {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("plan_id",strPlanId);
        map.put("superlike",strSuperLike);
        map.put("price",strPrice);
        map.put("request_id","1234");
        map.put("token",token);
        map.put("currency","USD");

        Call<SuccessResPurchaseSuperlike> call = apiInterface.makeSuperLikePayment(map);

        call.enqueue(new Callback<SuccessResPurchaseSuperlike>() {
            @Override
            public void onResponse(Call<SuccessResPurchaseSuperlike> call, Response<SuccessResPurchaseSuperlike> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResPurchaseSuperlike data = response.body();

                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        showToast(getActivity(), data.message);

                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResPurchaseSuperlike> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

}