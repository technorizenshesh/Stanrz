package com.technorizen.stanrz.utility;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.technorizen.stanrz.R;

/**
 * Created by Ravindra Birla on 10,July,2021
 */
public class BottomSheet extends BottomSheetDialogFragment {

    ImageView bottomSheet_cancelId;

    private Context context;

    private ReportInterface reportInterface;

    public BottomSheet(Context context,ReportInterface reportInterface)
    {
        this.context = context;
        this.reportInterface = reportInterface;
    }

  /*  public static BottomSheet newInstance() {
        BottomSheet fragment = new BottomSheet();
        return fragment;
    }*/

    @Override public int getTheme() {
        return R.style.AppBottomSheetDialogTheme;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setWhiteNavigationBar(@NonNull Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            GradientDrawable dimDrawable = new GradientDrawable();
            // ...customize your dim effect here

            GradientDrawable navigationBarDrawable = new GradientDrawable();
            navigationBarDrawable.setShape(GradientDrawable.RECTANGLE);
            navigationBarDrawable.setColor(Color.WHITE);

            Drawable[] layers = {dimDrawable, navigationBarDrawable};

            LayerDrawable windowBackground = new LayerDrawable(layers);
            windowBackground.setLayerInsetTop(1, metrics.heightPixels);

            window.setBackgroundDrawable(windowBackground);
        }
    }
/*
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Window window = getDialog().getWindow();
            window.findViewById(com.google.android.material.R.id.container).setFitsSystemWindows(false);
            // dark navigation bar icons
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet, null);
        dialog.setContentView(contentView);

        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

        TextView tvIDontLikeIt = dialog.findViewById(R.id.tvIDontLikeIt);
        TextView tvNudity = dialog.findViewById(R.id.tvNudity);
        TextView tvDangerousContent = dialog.findViewById(R.id.tvDangerousContent);
        TextView tvHateSpeech = dialog.findViewById(R.id.tvHateSpeech);
        TextView tvBulling = dialog.findViewById(R.id.tvBulling);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setWhiteNavigationBar(dialog);
        }

        tvIDontLikeIt.setOnClickListener(v ->
                {
                    reportInterface.onReport(tvIDontLikeIt.getText().toString(),"");
                }
                );

        tvNudity.setOnClickListener(v ->
                {
                    reportInterface.onReport(tvNudity.getText().toString(),"");
                }
        );

        tvDangerousContent.setOnClickListener(v ->
                {
                    reportInterface.onReport(tvDangerousContent.getText().toString(),"");
                }
        );

        tvHateSpeech.setOnClickListener(v ->
                {
                    reportInterface.onReport(tvHateSpeech.getText().toString(),"");
                }
        );

        tvBulling.setOnClickListener(v ->
                {
                    reportInterface.onReport(tvBulling.getText().toString(),"");
                }
        );

    }

}