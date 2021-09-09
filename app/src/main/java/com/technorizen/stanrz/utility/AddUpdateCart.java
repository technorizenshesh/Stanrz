package com.technorizen.stanrz.utility;

/**
 * Created by Ravindra Birla on 14,July,2021
 */
public interface AddUpdateCart {

    public void addCart(String resId,String itemId,String quantity);
    public void deleteCart(String userID,String itemId);

}
