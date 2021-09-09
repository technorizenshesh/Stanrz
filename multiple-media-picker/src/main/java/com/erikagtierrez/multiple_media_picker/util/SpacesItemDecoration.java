package com.erikagtierrez.multiple_media_picker.util;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    private int left,right,top,bottom;

    public SpacesItemDecoration(int space) {
        this.space = space;
        left = space;
        right=space;
        top=space;
        bottom=space;
    }
    public SpacesItemDecoration() {
    }
    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = left;
        outRect.right = right;
        outRect.bottom = bottom;
        outRect.top = top;

 /*   // Add top margin only for the first item to avoid double space between items
    if (parent.getChildLayoutPosition(view) == 0) {
        outRect.top = space;
    } else {
        outRect.top = 0;
    }*/
    }
}