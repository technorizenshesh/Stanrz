package com.technorizen.stanrz.activites;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.erikagtierrez.multiple_media_picker.Gallery;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.retrofit.Constant;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;

import static com.technorizen.stanrz.fragments.AddPostFragment.OPEN_MEDIA_PICKER;

public class SplashActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_CONSTANT = 5;
    private boolean isUserLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        isUserLoggedIn = SharedPreferenceUtility.getInstance(SplashActivity.this).getBoolean(Constant.IS_USER_LOGGED_IN);
        finds();

/*
        if(checkPermisssionForReadStorage())
        {
            Intent intent= new Intent(this, Gallery.class);
            // Set the title
            intent.putExtra("title","Select media");
            // Mode 1 for both images and videos selection, 2 for images only and 3 for videos!
            intent.putExtra("mode",1);
            intent.putExtra("maxSelection",1); // Optional
            startActivityForResult(intent,OPEN_MEDIA_PICKER);
        }
*/
    }

    //CHECKING FOR Camera STATUS
    public boolean checkPermisssionForReadStorage() {
        if (ContextCompat.checkSelfPermission(SplashActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(SplashActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ||

                ContextCompat.checkSelfPermission(SplashActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
        ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this,
                    Manifest.permission.CAMERA)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ) {

                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);
            } else {

                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);
            }
            return false;
        } else {

            //  explain("Please Allow Location Permission");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case MY_PERMISSION_CONSTANT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {


                    Intent intent= new Intent(this, Gallery.class);
                    // Set the title
                    intent.putExtra("title","Select media");
                    // Mode 1 for both images and videos selection, 2 for images only and 3 for videos!
                    intent.putExtra("mode",1);
                    intent.putExtra("maxSelection",1); // Optional
                    startActivityForResult(intent,OPEN_MEDIA_PICKER);

                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read_external_storage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean write_external_storage = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if (camera && read_external_storage && write_external_storage) {
                    } else {
                        Toast.makeText(SplashActivity.this, getResources().getString(R.string.permission_denied_boo), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(SplashActivity.this, getResources().getString(R.string.permission_denied_boo), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private void finds() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isUserLoggedIn) {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        },3000);
    }


}