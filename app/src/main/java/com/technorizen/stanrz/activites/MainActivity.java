package com.technorizen.stanrz.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;

import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.ActivityMain2Binding;

import static com.technorizen.stanrz.activites.LoginActivity.TAG;

public class MainActivity extends AppCompatActivity {

    ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main2);
//        binding.ivTag.setTag();

        String myString = "anna";

        StringBuffer stringBuffer = new StringBuffer(myString);

        String myString1 ="";

        for(int i=myString.length()-1;i>=0;i--)
        {

            myString1 = myString1+myString.charAt(i);

        }

        Log.d(TAG, "onCreate: "+myString.contains(myString1));


    }
}