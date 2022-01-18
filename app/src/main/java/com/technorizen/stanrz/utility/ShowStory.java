package com.technorizen.stanrz.utility;

import android.view.View;

import com.technorizen.stanrz.models.SuccessResGetStories;

import java.util.List;

/**
 * Created by Ravindra Birla on 14,July,2021
 */
public interface ShowStory {

    public void showStory(View v,int pos,String userName, String userImage, SuccessResGetStories.Result story);

}
