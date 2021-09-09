package com.technorizen.stanrz.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.ActivityForgotPasswordBinding;
import com.technorizen.stanrz.models.SuccessResForgetPassword;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.NetworkAvailablity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technorizen.stanrz.retrofit.Constant.isValidEmail;
import static com.technorizen.stanrz.retrofit.Constant.showToast;

public class ForgotPasswordAct extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;
    private String strEmail = "";
    StanrzInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        apiInterface = ApiClient.getClient().create(StanrzInterface.class);

        binding.forgotAction.imgHeader.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });

        binding.forgotAction.tvHeader.setText("Forgot Password");

        binding.btnSend.setOnClickListener(v ->
                {

                    strEmail = binding.etEmail.getText().toString().trim();

                    if(isValid())
                    {

                        if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {
                            forgotPass();
                        } else {
                            Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(this, getResources().getString(R.string.on_error), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void forgotPass() {

        DataManager.getInstance().showProgressMessage(ForgotPasswordAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("email",strEmail);
        Call<SuccessResForgetPassword> call = apiInterface.forgotPassword(map);
        call.enqueue(new Callback<SuccessResForgetPassword>() {
            @Override
            public void onResponse(Call<SuccessResForgetPassword> call, Response<SuccessResForgetPassword> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResForgetPassword data = response.body();
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        Toast.makeText(ForgotPasswordAct.this,"Please check mail",Toast.LENGTH_SHORT).show();
//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);
                        binding.etEmail.setText("");
                    } else if (data.status.equals("0")) {
                        showToast(ForgotPasswordAct.this, data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResForgetPassword> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }


    private boolean isValid() {
        if (strEmail.equalsIgnoreCase("")) {
            binding.etEmail.setError("Please enter email.");
            return false;
        } else if (!isValidEmail(strEmail)) {
            binding.etEmail.setError("Please enter valid email.");
            return false;
        }
        return true;
    }


}