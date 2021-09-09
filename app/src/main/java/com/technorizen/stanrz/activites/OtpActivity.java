package com.technorizen.stanrz.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.ActivityOtpBinding;
import com.technorizen.stanrz.models.SuccessResSignUp;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.Constant;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technorizen.stanrz.activites.LoginActivity.TAG;
import static com.technorizen.stanrz.retrofit.Constant.showToast;

public class OtpActivity extends AppCompatActivity {

    StanrzInterface apiInterface;
    HashMap<String, String> map;
    private FirebaseAuth mAuth;
    private String mVerificationId,mobile;
    String strName="",strEmail="",strUserName="",strcc="",strdob="",strphone="",strPass="";
    Context mContext;
    String finalOtp = "";
    ActivityOtpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_otp);

        mContext = OtpActivity.this;
        Intent intent = getIntent();
         strName =  intent.getStringExtra("fullname");
        strUserName =  intent.getStringExtra("username");
        strEmail =  intent.getStringExtra("email");
        strPass =  intent.getStringExtra("password");
        strphone =  intent.getStringExtra("mobile");
        strdob =  intent.getStringExtra("dob");

        mAuth = FirebaseAuth.getInstance();

        apiInterface = ApiClient.getClient().create(StanrzInterface.class);

        mobile = "+"+strphone;

        init();

        VerifyPhoneNumber();

        binding.tvResentToMessage.setOnClickListener(v ->
                {
                    VerifyPhoneNumber();
                }
                );

        binding.btnSignup.setOnClickListener(v ->
                {

                    if (TextUtils.isEmpty(binding.et1.getText().toString().trim())) {
                        Toast.makeText(mContext, getString(R.string.please_enter_com_otp), Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(binding.et2.getText().toString().trim())) {
                        Toast.makeText(mContext, getString(R.string.please_enter_com_otp), Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(binding.et3.getText().toString().trim())) {
                        Toast.makeText(mContext, getString(R.string.please_enter_com_otp), Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(binding.et4.getText().toString().trim())) {
                        Toast.makeText(mContext, getString(R.string.please_enter_com_otp), Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(binding.et5.getText().toString().trim())) {
                        Toast.makeText(mContext, getString(R.string.please_enter_com_otp), Toast.LENGTH_SHORT).show();
                    }else if (TextUtils.isEmpty(binding.et6.getText().toString().trim())) {
                        Toast.makeText(mContext, getString(R.string.please_enter_com_otp), Toast.LENGTH_SHORT).show();
                    }else {
                        finalOtp =
                                binding.et1.getText().toString().trim() +
                                        binding.et2.getText().toString().trim() +
                                        binding.et3.getText().toString().trim() +
                                        binding.et4.getText().toString().trim()+
                                        binding.et5.getText().toString().trim()+
                                        binding.et6.getText().toString().trim()
                        ;

                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, finalOtp);
                        signInWithPhoneAuthCredential(credential);

                    }
                }
        );

    }
/*

    private void VerifyPhoneNumber(){
        binding.tvResentToMessage.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);

        // mobile = "+2348036624845";
        Log.e("mobilenumber====",mobile);
      */
/*  PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+"+session.getStringValue(SessionKey.mobile))       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);*//*

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile.replace(" ", ""), 60,  TimeUnit.SECONDS,  OtpActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    // Phone number to verify
                    // Timeout duration
                    // Unit of timeout
                    // Activity (for callback binding)

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        Toast.makeText(OtpActivity.this, "Otp send successfully.", Toast.LENGTH_SHORT).show();

                        Log.e(TAG, "onCodeSent:" + verificationId);
                        binding.tvError.setVisibility(View.VISIBLE);
                        binding.tvError.setText("Code Sent Successfully");
                        binding.tvResentToMessage.setVisibility(View.VISIBLE);
                        // Save verification ID and resending token so we can use them later
                        mVerificationId = verificationId;

                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                        //   ProjectUtil.pauseProgressDialog()
                        //      DataManager.getInstance().hideProgressMessage();
                        //     Toast.makeText(OTPActivity.this, ""+phoneAuthCredential.getSmsCode(), Toast.LENGTH_SHORT).show();
                        //        signInWithPhoneAuthCredential(phoneAuthCredential);
                        Log.d(TAG, "onVerificationCompleted:" + credential);
                        binding.tvResentToMessage.setVisibility(View.VISIBLE);
//                        binding.progressBar.setVisibility(View.GONE);
                        signInWithPhoneAuthCredential(credential);

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        // ProjectUtil.pauseProgressDialog();
                        //   DataManager.getInstance().hideProgressMessage();
                        //   Toast.makeText(OTPActivity.this, "Failed"+e, Toast.LENGTH_SHORT).show();

                        binding.tvResentToMessage.setVisibility(View.VISIBLE);
//                        binding.progressBar.setVisibility(View.GONE);
                        // This callback is invoked in an invalid request for verification is made,
                        // for instance if the the phone number format is not valid.
                        Log.w(TAG, "onVerificationFailed", e);

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                            binding.tvError.setVisibility(View.VISIBLE);
                            binding.tvError.setText(e.getLocalizedMessage());

                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // The SMS quota for the project has been exceeded
                            binding.tvError.setVisibility(View.VISIBLE);
                            binding.tvError.setText(e.getLocalizedMessage());
                        }
                    }
                });

    }
*/

    private void VerifyPhoneNumber(){
        binding.tvResentToMessage.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
      //  mobile = "+"+session.getStringValue(SessionKey.mobile);

        // mobile = "+2348036624845";
        Log.e("mobilenumber====",mobile);
      /*  PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+"+session.getStringValue(SessionKey.mobile))       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);*/

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile.replace(" ", ""), 60,  TimeUnit.SECONDS,  OtpActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    // Phone number to verify
                    // Timeout duration
                    // Unit of timeout
                    // Activity (for callback binding)

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        Toast.makeText(OtpActivity.this, "Otp send successfully.", Toast.LENGTH_SHORT).show();

                        Log.e(TAG, "onCodeSent:" + verificationId);
                        binding.tvError.setVisibility(View.VISIBLE);
                        binding.tvError.setText("Code Sent Successfully");
                        binding.tvResentToMessage.setVisibility(View.VISIBLE);
                        binding.progressBar.setVisibility(View.GONE);
                        // Save verification ID and resending token so we can use them later
                        mVerificationId = verificationId;

                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                        //   ProjectUtil.pauseProgressDialog()
                        //      DataManager.getInstance().hideProgressMessage();
                        //     Toast.makeText(OTPActivity.this, ""+phoneAuthCredential.getSmsCode(), Toast.LENGTH_SHORT).show();
                        //        signInWithPhoneAuthCredential(phoneAuthCredential);
                        Log.d(TAG, "onVerificationCompleted:" + credential);
                        binding.tvResentToMessage.setVisibility(View.VISIBLE);
                        binding.progressBar.setVisibility(View.GONE);
                        signInWithPhoneAuthCredential(credential);

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        // ProjectUtil.pauseProgressDialog();
                        //   DataManager.getInstance().hideProgressMessage();
                        //   Toast.makeText(OTPActivity.this, "Failed"+e, Toast.LENGTH_SHORT).show();

                        binding.tvResentToMessage.setVisibility(View.VISIBLE);
                        binding.progressBar.setVisibility(View.GONE);
                        // This callback is invoked in an invalid request for verification is made,
                        // for instance if the the phone number format is not valid.
                        Log.w(TAG, "onVerificationFailed", e);

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                            binding.tvError.setVisibility(View.VISIBLE);
                            binding.tvError.setText(e.getLocalizedMessage());

                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // The SMS quota for the project has been exceeded
                            binding.tvError.setVisibility(View.VISIBLE);
                            binding.tvError.setText(e.getLocalizedMessage());
                        }
                    }
                });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(OtpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            binding.tvError.setVisibility(View.VISIBLE);
                            binding.tvError.setText("Success");
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            // Update UI
//                            UpdateOtpToserver(credential.getSmsCode());

                            signup();


                            //   session.setIsVerify();
                            //    startActivity(new Intent(getContext(),HomeActivity.class));
                            //     getActivity().finish();
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                binding.tvError.setVisibility(View.VISIBLE);
                                binding.tvError.setText(task.getException().getLocalizedMessage());
                            }
                        }
                    }
                });
    }


    private void signup()
    {

        DataManager.getInstance().showProgressMessage(OtpActivity.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("fullname",strName);
        map.put("username",strUserName);
        map.put("email",strEmail);
        map.put("password",strPass);
        map.put("mobile",strphone);
        map.put("dob",strdob);
        map.put("register_id","");

        Call<SuccessResSignUp> signupCall = apiInterface.signup(map);
        signupCall.enqueue(new Callback<SuccessResSignUp>() {
            @Override
            public void onResponse(Call<SuccessResSignUp> call, Response<SuccessResSignUp> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResSignUp data = response.body();
                    if (data.status.equals("1")) {
                        showToast(OtpActivity.this, data.message);
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        SharedPreferenceUtility.getInstance(getApplication()).putBoolean(Constant.IS_USER_LOGGED_IN, true);
                        SharedPreferenceUtility.getInstance(OtpActivity.this).putString(Constant.USER_ID,data.getResult().getId());

                        //   mobileVerify();
//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);
                        startActivity(new Intent(OtpActivity.this, LoginActivity.class));
                        // finish();
                    } else if (data.status.equals("0")) {
                        showToast(OtpActivity.this, data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResSignUp> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }

        });

    }
    private void init() {


        binding.et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.et2.setText("");
                    binding.et2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }


        });

        binding.et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.et3.setText("");
                    binding.et3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }


        });

        binding.et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.et4.setText("");
                    binding.et4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }




        });

        binding.et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.et5.setText("");
                    binding.et5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }




        });

        binding.et5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.et6.setText("");
                    binding.et6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }




        });


        binding.et6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }


        });

    }

}