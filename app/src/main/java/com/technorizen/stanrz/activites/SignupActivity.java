package com.technorizen.stanrz.activites;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;

import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.gson.Gson;
import com.ozcanalasalvar.library.view.datePicker.DatePicker;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.ActivitySignupBinding;
import com.technorizen.stanrz.models.SuccessResSignUp;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.Constant;
import com.technorizen.stanrz.retrofit.NetworkAvailablity;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.technorizen.stanrz.retrofit.Constant.emailPattern;
import static com.technorizen.stanrz.retrofit.Constant.isValidEmail;
import static com.technorizen.stanrz.retrofit.Constant.showToast;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    StanrzInterface apiInterface;

    String strName="",strEmail="",strUserName="",strcc="",strdob="",strphone="",strPass="",strConfirmPass="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiInterface = ApiClient.getClient().create(StanrzInterface.class);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        binding.btnReg.setOnClickListener(v ->

                {
                    strName = binding.etFullName.getText().toString().trim();
                    strUserName = binding.etUserName.getText().toString().trim();
                    strEmail = binding.etEmail.getText().toString().trim();
                    strPass = binding.etPassword.getText().toString().trim();
                    strcc = binding.ccp.getSelectedCountryCode();
                    strphone = strcc+binding.etPhone.getText().toString().trim();
                    strConfirmPass = binding.etConfPass.getText().toString().trim();

                    if (isValid()) {

                        if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {

                            signup();

                        } else {
                            Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.on_error), Toast.LENGTH_SHORT).show();

                    }

                }
        );
        binding.rlbottom.setOnClickListener(v ->
                {
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                }
        );

        binding.etDOB.setOnClickListener(v ->
                {
                    try {
                        showDateSelection();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                );
        binding.header.setOnClickListener(v -> finish());

    }

    public void signup() {

//        strLat ="22.698986";
//        strLng = "75.867851";

        Intent intent = new Intent(SignupActivity.this, OtpActivity.class);
        intent.putExtra("fullname",  strName);
        intent.putExtra("username",  strUserName);
        intent.putExtra("email",  strEmail);
        intent.putExtra("password",  strPass);
        intent.putExtra("mobile",  strphone);
        intent.putExtra("dob",  strdob);

        startActivity(intent);

    }

    public void showDateSelection() throws ParseException {

        String dateStr = "04/05/2010";
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        SimpleDateFormat s1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat s2 = new SimpleDateFormat("ddMMyyyyHHmm");
        Date d = s1.parse("02/11/2012 23:11");
        String s3 = s2.format(d);
        System.out.println(s3);
        long l = Long.parseLong(s3);
        System.out.println(l);


        SimpleDateFormat s4 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat s5 = new SimpleDateFormat("ddMMyyyyHHmm");
        Date d1 = s4.parse("02/11/2030 23:11");
        String s6 = s5.format(d1);
        System.out.println(s6);
        long l1 = Long.parseLong(s6);
        System.out.println(l);


        final Dialog dialog = new Dialog(SignupActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_select_date);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        DatePicker datePicker = dialog.findViewById(R.id.datepicker);
        datePicker.setOffset(3);
        datePicker.setTextSize(19);
        datePicker.setPickerMode(DatePicker.DAY_ON_FIRST);
        AppCompatButton btnSave = dialog.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v ->
                {

                    long val = datePicker.getDate();
                    Date date=new Date(val);
                    SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
                    String dateText = df2.format(date);

                    binding.etDOB.setText(dateText);

                    strdob = dateText;

                    dialog.dismiss();

                }
                );

            datePicker.setDataSelectListener(new DatePicker.DataSelectListener() {
            @Override
            public void onDateSelected(long date, int day, int month, int year) {


                long val = date;
                Date date1=new Date(val);
                SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
                String dateText = df2.format(date1);

                binding.etDOB.setText(dateText);

                strdob = dateText;

                Log.d(TAG, "onDateSelected: "+date);

            }
        });


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    private boolean isValid() {
        if (strName.equalsIgnoreCase("")) {
            binding.etFullName.setError(getString(R.string.enter_first));
            return false;
        } else if (strUserName.equalsIgnoreCase("")) {
            binding.etUserName.setError(getString(R.string.enter_last));
            return false;
        }else if (strEmail.equalsIgnoreCase("")) {
            binding.etEmail.setError(getString(R.string.enter_email));
            return false;
        }  else if (!isValidEmail(strEmail)) {
            binding.etEmail.setError(getString(R.string.enter_valid_email));
            return false;
        }else if (strphone.equalsIgnoreCase("")) {
            binding.etPhone.setError(getString(R.string.please_enter_mobile_number));
            return false;
        }else if (strPass.equalsIgnoreCase("")) {
            binding.etPassword.setError(getString(R.string.please_enter_pass));
            return false;
        } else if (strConfirmPass.equalsIgnoreCase("")) {
            binding.etConfPass.setError(getString(R.string.please_enter_con_pass));
            return false;
        } else if (!strPass.equalsIgnoreCase(strConfirmPass)) {
            binding.etConfPass.setError(getString(R.string.pass_and_confirm_pass_not_macthed));
            return false;
        }else if(!binding.checkBox.isChecked())
        {
            Toast.makeText(this,"Please agree on terms and conditions", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}