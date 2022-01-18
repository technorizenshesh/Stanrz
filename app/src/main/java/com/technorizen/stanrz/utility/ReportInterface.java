package com.technorizen.stanrz.utility;

/**
 * Created by Ravindra Birla on 18,October,2021
 */
public interface ReportInterface {

    public void onReport(String content,String userId);

    public void deleteChat(String userId);

    public void blockUser(String oneId,String OtherId);

}
