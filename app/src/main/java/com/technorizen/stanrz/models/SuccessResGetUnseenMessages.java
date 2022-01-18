package com.technorizen.stanrz.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class SuccessResGetUnseenMessages implements Serializable {

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
        @SerializedName("sender_id")
        @Expose
        public String senderId;
        @SerializedName("receiver_id")
        @Expose
        public String receiverId;
        @SerializedName("chat_message")
        @Expose
        public String chatMessage;
        @SerializedName("image_video")
        @Expose
        public String imageVideo;
        @SerializedName("caption")
        @Expose
        public String caption;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("chat_image")
        @Expose
        public String chatImage;
        @SerializedName("chat_audio")
        @Expose
        public String chatAudio;
        @SerializedName("chat_video")
        @Expose
        public String chatVideo;
        @SerializedName("chat_document")
        @Expose
        public String chatDocument;
        @SerializedName("lat")
        @Expose
        public String lat;
        @SerializedName("lon")
        @Expose
        public String lon;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("contact")
        @Expose
        public String contact;
        @SerializedName("clear_chat")
        @Expose
        public String clearChat;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("date")
        @Expose
        public String date;
        @SerializedName("time_zone")
        @Expose
        public String timeZone;
        @SerializedName("total_unseen_message")
        @Expose
        public Integer totalUnseenMessage;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public String getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(String receiverId) {
            this.receiverId = receiverId;
        }

        public String getChatMessage() {
            return chatMessage;
        }

        public void setChatMessage(String chatMessage) {
            this.chatMessage = chatMessage;
        }

        public String getImageVideo() {
            return imageVideo;
        }

        public void setImageVideo(String imageVideo) {
            this.imageVideo = imageVideo;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getChatImage() {
            return chatImage;
        }

        public void setChatImage(String chatImage) {
            this.chatImage = chatImage;
        }

        public String getChatAudio() {
            return chatAudio;
        }

        public void setChatAudio(String chatAudio) {
            this.chatAudio = chatAudio;
        }

        public String getChatVideo() {
            return chatVideo;
        }

        public void setChatVideo(String chatVideo) {
            this.chatVideo = chatVideo;
        }

        public String getChatDocument() {
            return chatDocument;
        }

        public void setChatDocument(String chatDocument) {
            this.chatDocument = chatDocument;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getClearChat() {
            return clearChat;
        }

        public void setClearChat(String clearChat) {
            this.clearChat = clearChat;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTimeZone() {
            return timeZone;
        }

        public void setTimeZone(String timeZone) {
            this.timeZone = timeZone;
        }

        public Integer getTotalUnseenMessage() {
            return totalUnseenMessage;
        }

        public void setTotalUnseenMessage(Integer totalUnseenMessage) {
            this.totalUnseenMessage = totalUnseenMessage;
        }

    }
    
}