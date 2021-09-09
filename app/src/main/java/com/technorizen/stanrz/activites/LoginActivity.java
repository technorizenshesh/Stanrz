package com.technorizen.stanrz.activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.facebook.CallbackManager;
import com.facebook.login.Login;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.ActivityLoginBinding;
import com.technorizen.stanrz.models.SuccessResSignIn;
import com.technorizen.stanrz.models.SuccessResSocialLogin;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.Constant;
import com.technorizen.stanrz.retrofit.NetworkAvailablity;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technorizen.stanrz.retrofit.Constant.showToast;

public class LoginActivity extends AppCompatActivity {


    String str_image_path = "";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    String strFullName = "",strUserName = "", strEmail = "", strdob = "", strLat = "", strLng = "",strSocialId ="",deviceToken = "",strPhoneNumber = "";
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    StanrzInterface apiInterface;
    public static String TAG = "LoginActivity";

    private ActivityLoginBinding binding;
    private String  strPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        apiInterface = ApiClient.getClient().create(StanrzInterface.class);

        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        getToken();

        binding.tvForgotPass.setOnClickListener(v ->
        {
        startActivity(new Intent(LoginActivity.this,ForgotPasswordAct.class));
        }
        );

        binding.llGoogle.setOnClickListener(v ->
                {
                    signIn();
                }
        );

        binding.btnSignin.setOnClickListener(v -> {

            strEmail = binding.etEmail.getText().toString().trim();
            strPassword = binding.etPass.getText().toString().trim();

            if (isValid()) {

                if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {

                    login();

                } else {
                    Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.on_error), Toast.LENGTH_SHORT).show();
            }

        });

        binding.rlLinkSignup.setOnClickListener(v ->
                {
                    startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                }
        );

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

                strFullName = account.getDisplayName();

                strUserName = account.getDisplayName();

                strEmail = account.getEmail();

                strSocialId = account.getId();

                socialLogin();

                // firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }

        mCallbackManager.onActivityResult(requestCode, resultCode, data);

    }

    public void socialLogin()

    {

//        strLat ="22.698986";
//        strLng = "75.867851";

        DataManager.getInstance().showProgressMessage(LoginActivity.this, getString(R.string.please_wait));

    /*    MultipartBody.Part filePart;
        if (!str_image_path.equalsIgnoreCase("")) {
            File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
            filePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }*/

        RequestBody fullName = RequestBody.create(MediaType.parse("text/plain"), strFullName);
        RequestBody userName = RequestBody.create(MediaType.parse("text/plain"), strUserName);
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"),strEmail);
        RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), strPhoneNumber);
        RequestBody dob = RequestBody.create(MediaType.parse("text/plain"), strdob);
        RequestBody registerID = RequestBody.create(MediaType.parse("text/plain"),deviceToken);
        RequestBody socialId = RequestBody.create(MediaType.parse("text/plain"),strSocialId);

        Call<SuccessResSocialLogin> signupCall = apiInterface.socialLogin(fullName,userName,email,mobile,dob,registerID,socialId);
        signupCall.enqueue(new Callback<SuccessResSocialLogin>() {
            @Override
            public void onResponse(Call<SuccessResSocialLogin> call, Response<SuccessResSocialLogin> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResSocialLogin data = response.body();
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        SharedPreferenceUtility.getInstance(getApplication()).putBoolean(Constant.IS_USER_LOGGED_IN, true);
                        SharedPreferenceUtility.getInstance(LoginActivity.this).putString(Constant.USER_ID,data.getResult().getId());
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);
                        finish();
                    } else if (data.status.equals("0")) {
                        showToast(LoginActivity.this, data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResSocialLogin> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }

        });
    }

    private void login() {

        DataManager.getInstance().showProgressMessage(LoginActivity.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("username", strEmail);
        map.put("password", strPassword);
        map.put("register_id", deviceToken);

      /*  RequestBody email = RequestBody.create(MediaType.parse("text/plain"),strEmail);
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), strPassword);
        RequestBody registerID = RequestBody.create(MediaType.parse("text/plain"),deviceToken);
*/
//        Call<SuccessResSignIn> call = apiInterface.login(email,password,registerID);
        Call<SuccessResSignIn> call = apiInterface.login(map);

        call.enqueue(new Callback<SuccessResSignIn>() {
            @Override
            public void onResponse(Call<SuccessResSignIn> call, Response<SuccessResSignIn> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResSignIn data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        SharedPreferenceUtility.getInstance(getApplication()).putBoolean(Constant.IS_USER_LOGGED_IN, true);
                        SharedPreferenceUtility.getInstance(LoginActivity.this).putString(Constant.USER_ID, data.getResult().getId());
                        Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);
                        finish();
                    } else if (data.status.equals("0")) {
                        showToast(LoginActivity.this, data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResSignIn> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private boolean isValid() {
        if (strEmail.equalsIgnoreCase("")) {
            binding.etEmail.setError(getString(R.string.enter_email));
            return false;
        } else if (strPassword.equalsIgnoreCase("")) {
            binding.etPass.setError(getString(R.string.please_enter_pass));
            return false;
        }
        return true;
    }


    /**
     * This method is used to get fcm token
     */
    private void getToken() {
        try {
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                return;
                            }
                            // Get new FCM registration token
                            String token = task.getResult();
                            deviceToken = token;
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, "Error=>" + e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}