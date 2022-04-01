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
        @SerializedName("fullname")
        @Expose
        public String fullname;
        @SerializedName("username")
        @Expose
        public String username;
        @SerializedName("mobile")
        @Expose
        public String mobile;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("password")
        @Expose
        public String password;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("bg_image")
        @Expose
        public String bgImage;
        @SerializedName("online_status")
        @Expose
        public String onlineStatus;
        @SerializedName("address")
        @Expose
        public String address;
        @SerializedName("website")
        @Expose
        public String website;
        @SerializedName("facebook")
        @Expose
        public String facebook;
        @SerializedName("youtube")
        @Expose
        public String youtube;
        @SerializedName("instagram")
        @Expose
        public String instagram;
        @SerializedName("dob")
        @Expose
        public String dob;
        @SerializedName("bio")
        @Expose
        public String bio;
        @SerializedName("language")
        @Expose
        public String language;
        @SerializedName("interested")
        @Expose
        public String interested;
        @SerializedName("gender")
        @Expose
        public String gender;
        @SerializedName("total_superlikes")
        @Expose
        public String totalSuperlikes;
        @SerializedName("total_coins")
        @Expose
        public String totalCoins;
        @SerializedName("total_p_coins")
        @Expose
        public String totalPCoins;
        @SerializedName("fan_club")
        @Expose
        public String fanClub;
        @SerializedName("time_zone")
        @Expose
        public String timeZone;
        @SerializedName("stanrz_of")
        @Expose
        public String stanrzOf;
        @SerializedName("wallet")
        @Expose
        public String wallet;
        @SerializedName("email_code")
        @Expose
        public String emailCode;
        @SerializedName("passkey")
        @Expose
        public String passkey;
        @SerializedName("OpenFanClub")
        @Expose
        public String openFanClub;
        @SerializedName("register_id")
        @Expose
        public String registerId;
        @SerializedName("social_id")
        @Expose
        public String socialId;
        @SerializedName("sid")
        @Expose
        public String sid;
        @SerializedName("datetime")
        @Expose
        public String datetime;
        @SerializedName("posttype")
        @Expose
        public String posttype;
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
        @SerializedName("subscriber_post")
        @Expose
        public String subscriberPost;
        @SerializedName("total_comment")
        @Expose
        public Integer totalComment;
        @SerializedName("time_ago")
        @Expose
        public String timeAgo;
        @SerializedName("liked")
        @Expose
        public String liked;
        @SerializedName("saved")
        @Expose
        public String saved;
        @SerializedName("tag_users_details")
        @Expose
        public List<String> tagUsersDetails = null;
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

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getBgImage() {
            return bgImage;
        }

        public void setBgImage(String bgImage) {
            this.bgImage = bgImage;
        }

        public String getOnlineStatus() {
            return onlineStatus;
        }

        public void setOnlineStatus(String onlineStatus) {
            this.onlineStatus = onlineStatus;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getFacebook() {
            return facebook;
        }

        public void setFacebook(String facebook) {
            this.facebook = facebook;
        }

        public String getYoutube() {
            return youtube;
        }

        public void setYoutube(String youtube) {
            this.youtube = youtube;
        }

        public String getInstagram() {
            return instagram;
        }

        public void setInstagram(String instagram) {
            this.instagram = instagram;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getInterested() {
            return interested;
        }

        public void setInterested(String interested) {
            this.interested = interested;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getTotalSuperlikes() {
            return totalSuperlikes;
        }

        public void setTotalSuperlikes(String totalSuperlikes) {
            this.totalSuperlikes = totalSuperlikes;
        }

        public String getTotalCoins() {
            return totalCoins;
        }

        public void setTotalCoins(String totalCoins) {
            this.totalCoins = totalCoins;
        }

        public String getTotalPCoins() {
            return totalPCoins;
        }

        public void setTotalPCoins(String totalPCoins) {
            this.totalPCoins = totalPCoins;
        }

        public String getFanClub() {
            return fanClub;
        }

        public void setFanClub(String fanClub) {
            this.fanClub = fanClub;
        }

        public String getTimeZone() {
            return timeZone;
        }

        public void setTimeZone(String timeZone) {
            this.timeZone = timeZone;
        }

        public String getStanrzOf() {
            return stanrzOf;
        }

        public void setStanrzOf(String stanrzOf) {
            this.stanrzOf = stanrzOf;
        }

        public String getWallet() {
            return wallet;
        }

        public void setWallet(String wallet) {
            this.wallet = wallet;
        }

        public String getEmailCode() {
            return emailCode;
        }

        public void setEmailCode(String emailCode) {
            this.emailCode = emailCode;
        }

        public String getPasskey() {
            return passkey;
        }

        public void setPasskey(String passkey) {
            this.passkey = passkey;
        }

        public String getOpenFanClub() {
            return openFanClub;
        }

        public void setOpenFanClub(String openFanClub) {
            this.openFanClub = openFanClub;
        }

        public String getRegisterId() {
            return registerId;
        }

        public void setRegisterId(String registerId) {
            this.registerId = registerId;
        }

        public String getSocialId() {
            return socialId;
        }

        public void setSocialId(String socialId) {
            this.socialId = socialId;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public String getPosttype() {
            return posttype;
        }

        public void setPosttype(String posttype) {
            this.posttype = posttype;
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

        public String getSubscriberPost() {
            return subscriberPost;
        }

        public void setSubscriberPost(String subscriberPost) {
            this.subscriberPost = subscriberPost;
        }

        public Integer getTotalComment() {
            return totalComment;
        }

        public void setTotalComment(Integer totalComment) {
            this.totalComment = totalComment;
        }

        public String getTimeAgo() {
            return timeAgo;
        }

        public void setTimeAgo(String timeAgo) {
            this.timeAgo = timeAgo;
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

    }
    
}

