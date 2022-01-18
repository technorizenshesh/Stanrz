package com.technorizen.stanrz.models;

import android.widget.VideoView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ravindra Birla on 16,September,2021
 */
public class UserPost implements Serializable {

    public String id;
    public String postId;
    public String dateTime;

    private boolean isPlaying;

    private VideoView player;

    public boolean isPlaying() {
        return isPlaying;
    }

    public String postData;

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String postType;


    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public VideoView getPlayer() {
        return player;
    }

    public void setPlayer(VideoView player) {
        this.player = player;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }


    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    public String getPostData() {
        return postData;
    }

    public void setPostData(String postData) {
        this.postData = postData;
    }
}