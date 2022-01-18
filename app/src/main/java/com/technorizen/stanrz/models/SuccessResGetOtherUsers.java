package com.technorizen.stanrz.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SuccessResGetOtherUsers implements Serializable {

    @SerializedName("result")
    @Expose
    public Result result;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public String status;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
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
        @SerializedName("type")
        @Expose
        public String type;
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
        @SerializedName("status")
        @Expose
        public String status;
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
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("follow")
        @Expose
        public String follow;
        @SerializedName("verified")
        @Expose
        public String verified;
        @SerializedName("subscriber")
        @Expose
        public String subscriber;
        @SerializedName("total_post")
        @Expose
        public Integer totalPost;
        @SerializedName("total_followers")
        @Expose
        public Integer totalFollowers;
        @SerializedName("total_following")
        @Expose
        public Integer totalFollowing;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getFollow() {
            return follow;
        }

        public void setFollow(String follow) {
            this.follow = follow;
        }

        public String getVerified() {
            return verified;
        }

        public void setVerified(String verified) {
            this.verified = verified;
        }

        public String getSubscriber() {
            return subscriber;
        }

        public void setSubscriber(String subscriber) {
            this.subscriber = subscriber;
        }

        public Integer getTotalPost() {
            return totalPost;
        }

        public void setTotalPost(Integer totalPost) {
            this.totalPost = totalPost;
        }

        public Integer getTotalFollowers() {
            return totalFollowers;
        }

        public void setTotalFollowers(Integer totalFollowers) {
            this.totalFollowers = totalFollowers;
        }

        public Integer getTotalFollowing() {
            return totalFollowing;
        }

        public void setTotalFollowing(Integer totalFollowing) {
            this.totalFollowing = totalFollowing;
        }

    }
    
}

