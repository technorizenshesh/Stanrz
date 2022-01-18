package com.technorizen.stanrz.utility;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by Ravindra Birla on 09,December,2021
 */
public class MySpannable extends ClickableSpan {

    private boolean isUnderline = true;


    public MySpannable(boolean isUnderline) {
        this.isUnderline = isUnderline;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(isUnderline);
        ds.setColor(Color.parseColor("#1b76d3"));
    }

    @Override
    public void onClick(View widget) {


    }
}