package com.technorizen.stanrz.utility;

import android.view.View;

import com.technorizen.stanrz.models.Result;

/**
 * Created by Ravindra Birla on 14,July,2021
 */
public interface AddLike {

    public void addLike(String post_id,String uploader_id);

    public void comment(View v, String postID);

    public void bottomSheet(View param1, String postID, boolean isUser, int position, Result result);

    public void addSuperLikes(View v,String postId,String uploaderId,String quantity);

    public void unlockNsfw(View v,String postId,String uploaderId,String quantity);


}
