package com.technorizen.stanrz.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SuccessResGetUploadedVideos implements Serializable {

    @SerializedName("result")
    @Expose
    public List<Result> result = null;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public String status;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class Result {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("user_id")
        @Expose
        public String userId;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("comment")
        @Expose
        public String comment;
        @SerializedName("unlock_with")
        @Expose
        public String unlockWith;
        @SerializedName("nsfw")
        @Expose
        public String nsfw;
        @SerializedName("tagusers")
        @Expose
        public String tagusers;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("user_image")
        @Expose
        public String userImage;
        @SerializedName("user_name")
        @Expose
        public String userName;
        @SerializedName("total_like")
        @Expose
        public Integer totalLike;
        @SerializedName("total_super_like")
        @Expose
        public String totalSuperLike;
        @SerializedName("uploaded_as")
        @Expose
        public String uploadedAs;
        @SerializedName("post_is")
        @Expose
        public String postIs;
        @SerializedName("total_comment")
        @Expose
        public Integer totalComment;
        @SerializedName("liked")
        @Expose
        public String liked;
        @SerializedName("saved")
        @Expose
        public String saved;
        @SerializedName("tag_users_details")
        @Expose
        public List<String> tagUsersDetails = null;
        @SerializedName("time_ago")
        @Expose
        public String timeAgo;
        @SerializedName("user_post")
        @Expose
        public List<UserPost> userPost = null;

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

        public String getUnlockWith() {
            return unlockWith;
        }

        public void setUnlockWith(String unlockWith) {
            this.unlockWith = unlockWith;
        }

        public String getNsfw() {
            return nsfw;
        }

        public void setNsfw(String nsfw) {
            this.nsfw = nsfw;
        }

        public String getTagusers() {
            return tagusers;
        }

        public void setTagusers(String tagusers) {
            this.tagusers = tagusers;
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

        public String getTotalSuperLike() {
            return totalSuperLike;
        }

        public void setTotalSuperLike(String totalSuperLike) {
            this.totalSuperLike = totalSuperLike;
        }

        public String getUploadedAs() {
            return uploadedAs;
        }

        public void setUploadedAs(String uploadedAs) {
            this.uploadedAs = uploadedAs;
        }

        public String getPostIs() {
            return postIs;
        }

        public void setPostIs(String postIs) {
            this.postIs = postIs;
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

        public String getSaved() {
            return saved;
        }

        public void setSaved(String saved) {
            this.saved = saved;
        }

        public List<String> getTagUsersDetails() {
            return tagUsersDetails;
        }

        public void setTagUsersDetails(List<String> tagUsersDetails) {
            this.tagUsersDetails = tagUsersDetails;
        }

        public String getTimeAgo() {
            return timeAgo;
        }

        public void setTimeAgo(String timeAgo) {
            this.timeAgo = timeAgo;
        }

        public List<UserPost> getUserPost() {
            return userPost;
        }

        public void setUserPost(List<UserPost> userPost) {
            this.userPost = userPost;
        }

    }
    
    
    public class UserPost {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("post_id")
        @Expose
        public String postId;
        @SerializedName("post_data")
        @Expose
        public String postData;
        @SerializedName("post_type")
        @Expose
        public String postType;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("total_image")
        @Expose
        public Integer totalImage;

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

        public String getPostData() {
            return postData;
        }

        public void setPostData(String postData) {
            this.postData = postData;
        }

        public String getPostType() {
            return postType;
        }

        public void setPostType(String postType) {
            this.postType = postType;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public Integer getTotalImage() {
            return totalImage;
        }

        public void setTotalImage(Integer totalImage) {
            this.totalImage = totalImage;
        }

    }
    
}

