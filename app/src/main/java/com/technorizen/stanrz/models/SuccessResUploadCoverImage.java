package com.technorizen.stanrz.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SuccessResUploadCoverImage implements Serializable {

    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

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
        private String id;
        @SerializedName("fullname")
        @Expose
        private String fullname;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("bg_image")
        @Expose
        private String bgImage;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("online_status")
        @Expose
        private String onlineStatus;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("website")
        @Expose
        private String website;
        @SerializedName("facebook")
        @Expose
        private String facebook;
        @SerializedName("youtube")
        @Expose
        private String youtube;
        @SerializedName("instagram")
        @Expose
        private String instagram;
        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("bio")
        @Expose
        private String bio;
        @SerializedName("language")
        @Expose
        private String language;
        @SerializedName("interested")
        @Expose
        private String interested;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("email_code")
        @Expose
        private String emailCode;
        @SerializedName("passkey")
        @Expose
        private String passkey;
        @SerializedName("register_id")
        @Expose
        private String registerId;
        @SerializedName("social_id")
        @Expose
        private String socialId;
        @SerializedName("date_time")
        @Expose
        private String dateTime;

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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

    }

}

