package com.technorizen.stanrz.models;

import android.media.MediaPlayer;
import android.widget.VideoView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ravindra Birla on 16,September,2021
 */
public class Result implements Serializable {

    public String id;
    public String userId;
    public String description;
    public String comment;
    public String status;
    public String type;
    public String dateTime;
    public String userImage;
    public String userName;
    public Integer totalLike;
    public Integer totalComment;
    public String liked;
    public Integer totalSuperLike;
    public transient List<UserPost> userPost = null;
    public String timeAgo;
    public String unlockWith;
    public String postIs;
    public String saved;

    public String uploadedAs;

    public List<String> tagUsersDetails = null;

    public String nsfw;

    private boolean isPlaying;

    private int visiblePercent;

    private VideoView player;

    private MediaPlayer mediaPlayer;

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public String getUploadedAs() {
        return uploadedAs;
    }

    public void setUploadedAs(String uploadedAs) {
        this.uploadedAs = uploadedAs;
    }

    public List<String> getTagUsersDetails() {
        return tagUsersDetails;
    }

    public void setTagUsersDetails(List<String> tagUsersDetails) {
        this.tagUsersDetails = tagUsersDetails;
    }


    public String getSaved() {
        return saved;
    }
    public String getPostIs() {
        return postIs;
    }

    public void setPostIs(String postIs) {
        this.postIs = postIs;
    }
    public void setSaved(String saved) {
        this.saved = saved;
    }
    public String getUnlockWith() {
        return unlockWith;
    }

    public String getNsfw() {
        return nsfw;
    }

    public void setNsfw(String nsfw) {
        this.nsfw = nsfw;
    }

    public void setUnlockWith(String unlockWith) {
        this.unlockWith = unlockWith;
    }
    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public VideoView getPlayer() {
        return player;
    }

    public void setPlayer(VideoView player) {
        this.player = player;
    }

    public int getVisiblePercent() {
        return visiblePercent;
    }

    public void setVisiblePercent(int visiblePercent) {
        this.visiblePercent = visiblePercent;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(Integer totalLike) {
        this.totalLike = totalLike;
    }

    public Integer getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(Integer totalComment) {
        this.totalComment = totalComment;
    }

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    public List<UserPost> getUserPost() {
        return userPost;
    }

    public void setUserPost(List<UserPost> userPost) {
        this.userPost = userPost;
    }

    public Integer getTotalSuperLike() {
        return totalSuperLike;
    }

    public void setTotalSuperLike(Integer totalSuperLike) {
        this.totalSuperLike = totalSuperLike;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

}
