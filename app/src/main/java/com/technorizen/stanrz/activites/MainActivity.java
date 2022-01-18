package com.technorizen.stanrz.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.ActivityMain2Binding;

public class MainActivity extends AppCompatActivity {

    ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main2);

//        binding.ivTag.setTag();

    }
}