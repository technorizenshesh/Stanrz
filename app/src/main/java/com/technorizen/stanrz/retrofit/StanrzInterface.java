package com.technorizen.stanrz.retrofit;

import com.technorizen.stanrz.models.SuccessResAddCard;
import com.technorizen.stanrz.models.SuccessResAddComment;
import com.technorizen.stanrz.models.SuccessResAddFollowing;
import com.technorizen.stanrz.models.SuccessResAddLike;
import com.technorizen.stanrz.models.SuccessResAddSubscription;
import com.technorizen.stanrz.models.SuccessResAddSuperLike;
import com.technorizen.stanrz.models.SuccessResApplyForFanClub;
import com.technorizen.stanrz.models.SuccessResCancelSubscription;
import com.technorizen.stanrz.models.SuccessResDeleteComment;
import com.technorizen.stanrz.models.SuccessResDeletePost;
import com.technorizen.stanrz.models.SuccessResDeleteStory;
import com.technorizen.stanrz.models.SuccessResExchangeCoinstoSuperlike;
import com.technorizen.stanrz.models.SuccessResForgetPassword;
import com.technorizen.stanrz.models.SuccessResGetBlockedUser;
import com.technorizen.stanrz.models.SuccessResGetCards;
import com.technorizen.stanrz.models.SuccessResGetChat;
import com.technorizen.stanrz.models.SuccessResGetComment;
import com.technorizen.stanrz.models.SuccessResGetConversation;
import com.technorizen.stanrz.models.SuccessResGetFollowers;
import com.technorizen.stanrz.models.SuccessResGetFollowings;
import com.technorizen.stanrz.models.SuccessResGetHelp;
import com.technorizen.stanrz.models.SuccessResGetMyPurchasedPlan;
import com.technorizen.stanrz.models.SuccessResGetNotifications;
import com.technorizen.stanrz.models.SuccessResGetOtherUsers;
import com.technorizen.stanrz.models.SuccessResGetPP;
import com.technorizen.stanrz.models.SuccessResGetPackages;
import com.technorizen.stanrz.models.SuccessResGetStanrzOf;
import com.technorizen.stanrz.models.SuccessResGetStories;
import com.technorizen.stanrz.models.SuccessResGetSuperLikePlan;
import com.technorizen.stanrz.models.SuccessResGetToken;
import com.technorizen.stanrz.models.SuccessResGetTransactionHistory;
import com.technorizen.stanrz.models.SuccessResGetUnseenMessages;
import com.technorizen.stanrz.models.SuccessResGetUnseenNotification;
import com.technorizen.stanrz.models.SuccessResGetUploadedVideos;
import com.technorizen.stanrz.models.SuccessResGetUploads;
import com.technorizen.stanrz.models.SuccessResGetUser;
import com.technorizen.stanrz.models.SuccessResGetUserActivity;
import com.technorizen.stanrz.models.SuccessResGetVideos;
import com.technorizen.stanrz.models.SuccessResHideShowPlan;
import com.technorizen.stanrz.models.SuccessResInsertChat;
import com.technorizen.stanrz.models.SuccessResProfileData;
import com.technorizen.stanrz.models.SuccessResPurchasePlan;
import com.technorizen.stanrz.models.SuccessResPurchaseSuperlike;
import com.technorizen.stanrz.models.SuccessResReportUser;
import com.technorizen.stanrz.models.SuccessResSavePost;
import com.technorizen.stanrz.models.SuccessResSignIn;
import com.technorizen.stanrz.models.SuccessResSignUp;
import com.technorizen.stanrz.models.SuccessResSocialLogin;
import com.technorizen.stanrz.models.SuccessResUnlockNsfw;
import com.technorizen.stanrz.models.SuccessResUpdateIamStanrz;
import com.technorizen.stanrz.models.SuccessResUpdateProfile;
import com.technorizen.stanrz.models.SuccessResUpdateVipMembership;
import com.technorizen.stanrz.models.SuccessResUploadCoverImage;
import com.technorizen.stanrz.models.SuccessResUploadPost;
import com.technorizen.stanrz.models.SuccessResUploadStory;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface StanrzInterface {

    @FormUrlEncoded
    @POST("signup")
    Call<SuccessResSignUp> signup(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("login")
    Call<SuccessResSignIn> login(@FieldMap Map<String, String> paramHashMap);
    @Multipart
    @POST("social_login")
    Call<SuccessResSocialLogin> socialLogin (@Part("fullname") RequestBody full_name,
                                             @Part("username") RequestBody user_name,
                                             @Part("email") RequestBody email,
                                             @Part("mobile") RequestBody mobile,
                                             @Part("dob") RequestBody dob,
                                             @Part("register_id") RequestBody registerId,
                                             @Part("social_id") RequestBody socialId,
                                             @Part("time_zone") RequestBody timezone
    );
    
    @FormUrlEncoded
    @POST("forgot_password")
    Call<SuccessResForgetPassword> forgotPassword(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_profile")
    Call<SuccessResProfileData> getProfile(@FieldMap Map<String, String> paramHashMap);
    @Multipart
    @POST("update_profile")
    Call<SuccessResUpdateProfile> updateProfile (@Part("user_id") RequestBody userId,
                                                 @Part("mobile") RequestBody mobile,
                                                 @Part("fullname") RequestBody fullname,
                                                 @Part("website") RequestBody address,
                                                 @Part("bio") RequestBody lat,
                                                 @Part("address") RequestBody lng,
                                                 @Part("gender") RequestBody gender,
                                                 @Part MultipartBody.Part file);
    @FormUrlEncoded
    @POST("change_password")
    Call<SuccessResForgetPassword> changePass(@FieldMap Map<String, String> paramsHashMap);
    @FormUrlEncoded
    @POST("search_user")
    Call<SuccessResGetUser> getUsers(@FieldMap Map<String, String> paramHashMap);
    @Multipart
    @POST("update_cover_image")
    Call<SuccessResUploadCoverImage> updateCoverPhoto (
            @Part("user_id") RequestBody userId,
            @Part MultipartBody.Part file);
    @FormUrlEncoded
    @POST("get_user_profile")
    Call<SuccessResGetOtherUsers> getOtherProfile(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("add_follow")
    Call<SuccessResAddFollowing> addFollowing(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("get_followers")
    Call<SuccessResGetFollowings> getFollowers(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("get_all_users")
    Call<SuccessResGetUser> getAll(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("get_all_users_new")
    Call<SuccessResGetUser> getAllNew(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_following")
    Call<SuccessResGetFollowings> getFollowings(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("remove_follow")
    Call<SuccessResAddFollowing> remove(@FieldMap Map<String, String> paramHashMap);

    @Multipart
    @POST("add_post")
    Call<SuccessResUploadPost> uploadPost (
            @Part("user_id") RequestBody userId,
            @Part("description") RequestBody description,
            @Part("comment") RequestBody comment,
            @Part("type") RequestBody type,
            @Part("nsfw") RequestBody nsfw,
            @Part("unlock_with") RequestBody unLoackWith,
            @Part("tagusers") RequestBody tagUsers,
            @Part List<MultipartBody.Part> file);

    @FormUrlEncoded
    @POST("get_other_following")
    Call<SuccessResGetFollowings> getOthersFollowing(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_other_followers")
    Call<SuccessResGetFollowings> getOthersFollower(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_post")
    Call<SuccessResGetUploadedVideos> getUploadedImageVideos(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("add_like")
    Call<SuccessResAddLike> addLike(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_other_user_post")
    Call<SuccessResGetUploadedVideos> getOtherUploadedImageVideos(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_comment")
    Call<SuccessResGetComment> getComments(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("add_post_comment")
    Call<ResponseBody> addComment(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_story")
    Call<SuccessResGetStories> getStories(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_all_story")
    Call<SuccessResGetStories> getAllStories(@FieldMap Map<String, String> paramHashMap);

    @Multipart
    @POST("add_video_post")
    Call<SuccessResGetVideos> uploadVideos (
            @Part("user_id") RequestBody userId,
            @Part("description") RequestBody description,
            @Part("comment") RequestBody comment,
            @Part("type") RequestBody type,
            @Part("nsfw") RequestBody nsfw,
            @Part("unlock_with") RequestBody unlock_with,
            @Part("tagusers") RequestBody tagUsers,
            @Part List<MultipartBody.Part> file);

    @FormUrlEncoded
    @POST("get_video_post")
    Call<SuccessResGetUploadedVideos> getVideos(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("get_other_user_video_post")
    Call<SuccessResGetUploadedVideos> getOtherUserVideos(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("get_home_post")
    Call<SuccessResGetUploadedVideos> getHomePost(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("get_notification")
    Call<SuccessResGetNotifications> getNotifications(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("get_conversation")
    Call<SuccessResGetConversation> getConversations(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("get_chat")
    Call<SuccessResGetChat> getChat(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("search_followers")
    Call<SuccessResGetUser> getMyFollowers(@FieldMap Map<String, String> paramHashMap);
    @Multipart
    @POST("add_story")
    Call<SuccessResUploadStory> uploadStory (
            @Part("user_id") RequestBody userId,
            @Part("caption") RequestBody description,
            @Part("story_type") RequestBody type,
            @Part List<MultipartBody.Part> file);
    @Multipart
    @POST("update_story")
    Call<SuccessResUploadStory> updateStory (
            @Part("user_id") RequestBody userID,
            @Part("story_id") RequestBody storyID,
            @Part("story_type") RequestBody type,
            @Part List<MultipartBody.Part> file);
    @FormUrlEncoded
    @POST("delete_single_story")
    Call<SuccessResDeleteStory> deleteStory(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("delete_post")
    Call<SuccessResDeletePost> deletePost(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("delete_comment")
    Call<SuccessResDeleteComment> deleteComment(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("add_post_superlike")
    Call<SuccessResAddSuperLike> addSuperLike(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_subscription_plan")
    Call<SuccessResGetPackages> getVipMemberShip(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("update_subscription_plan")
    Call<SuccessResUpdateVipMembership> editVipMembership(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("add_fan_membership")
    Call<SuccessResUpdateVipMembership> joinMembership(@FieldMap Map<String, String> paramHashMap);

    @Multipart
    @POST("apply_for_vip_membership")
    Call<SuccessResApplyForFanClub> applyVipMembership(
            @Part("user_id") RequestBody id,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part filepart,
            @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("search_chat_user")
    Call<SuccessResGetUser> getChatUsers(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("update_fan_club")
    Call<SuccessResUpdateVipMembership> updateFanClub(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("add_post_report")
    Call<SuccessResReportUser> reportPost(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("delete_chat")
    Call<SuccessResAddLike> deleteChat(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("save_post")
    Call<SuccessResSavePost> savePost(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("get_superlike_plan")
    Call<SuccessResGetSuperLikePlan> getSuperLikesPlan(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("superlike_payment")
    Call<SuccessResPurchaseSuperlike> makeSuperLikePayment(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("add_user_card")
    Call<SuccessResAddCard> addCard (@FieldMap Map<String, String> params);
    @FormUrlEncoded
    @POST("get_user_card")
    Call<SuccessResGetCards> getCards(@FieldMap Map<String, String> paramsHashMap);
    @FormUrlEncoded
    @POST("get_token")
    Call<SuccessResGetToken> getToken(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("exchange_coin_superlike")
    Call<SuccessResExchangeCoinstoSuperlike> exchangeCoinsToSuperLike(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("exchange_superlike_to_coin")
    Call<SuccessResExchangeCoinstoSuperlike> exchangeSuperLikeToCoins(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("exchange_coin_cash")
    Call<SuccessResExchangeCoinstoSuperlike> exchangeCoinsToCash(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("get_user_history")
    Call<SuccessResGetTransactionHistory> getTransactionHistory(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("get_save_post")
    Call<SuccessResGetUploadedVideos> getSavedImageVideos(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("add_user_report")
    Call<SuccessResReportUser> reportUser(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("block_user")
    Call<SuccessResAddLike> blockUser(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("get_all_post")
    Call<SuccessResGetUploadedVideos> getAllImagesAndVideos(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("add_user_subscription_plan")
    Call<SuccessResAddSuperLike> addSubscriptionPlan(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("purchase_subscription")
    Call<SuccessResPurchasePlan> joinVipMembership(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_user_purchase_subscription")
    Call<SuccessResGetMyPurchasedPlan> getUserPurchaseSubscription(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_my_subscriber")
    Call<SuccessResGetMyPurchasedPlan> getMySubscriber(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("suggestion_user")
    Call<SuccessResGetUser> getSuggestionUsers(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("delete_subscription_plan")
    Call<SuccessResAddLike> deleteSubscriptionPlan(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("privacy_policy")
    Call<SuccessResGetPP> getPrivacyPolicy(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_about_us")
    Call<SuccessResGetPP> getAbout(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_help_faq")
    Call<SuccessResGetHelp> getHelp(@FieldMap Map<String, String> paramHashMap);

    @Multipart
    @POST("insert_chat")
    Call<SuccessResInsertChat> insertImageVideoChat (
            @Part("sender_id") RequestBody senderId,
            @Part("receiver_id") RequestBody receiverId,
            @Part("chat_message") RequestBody chatMessage,
            @Part("type") RequestBody type,
            @Part("caption") RequestBody caption,
            @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("update_OpenFanClub")
    Call<SuccessResProfileData> approvedForFanPlan(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("add_subscription_description")
    Call<SuccessResAddSubscription> addDescription(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_subscription_description")
    Call<SuccessResAddSubscription> getDescription(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("fan_post")
    Call<SuccessResGetUploadedVideos> getFanUploads(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("search_following")
    Call<SuccessResGetFollowings> searchFollowing(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("search_followers")
    Call<SuccessResGetFollowings> searchFollower(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("post_for_fan")
    Call<SuccessResGetUploadedVideos> getFanPosts(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_unseen_notification")
    Call<SuccessResGetUnseenNotification> getUnseenNoti(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_unseen_message")
    Call<SuccessResGetUnseenMessages> getUnseenMessage(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_myfan_post")
    Call<SuccessResGetUploadedVideos> getFanClubImageVideos(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_top_stanrz")
    Call<SuccessResGetStanrzOf> getTopStanrz(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_stanrz_of")
    Call<SuccessResGetStanrzOf> getStanrzOf(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("update_stanrz_of")
    Call<SuccessResUpdateIamStanrz> updateEnableDisable(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_block_user")
    Call<SuccessResGetBlockedUser> getBlockedUser(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_user_activity")
    Call<SuccessResGetUserActivity> getUserActivity(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("update_nsfw_post")
    Call<SuccessResUnlockNsfw> unlockNSFW(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_post_details")
    Call<SuccessResGetUploadedVideos> getPostById(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("cancel_subscription")
    Call<SuccessResCancelSubscription> cancelSubscription(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("update_plan_view")
    Call<SuccessResHideShowPlan> hideSHowMonthlyPlan(@FieldMap Map<String, String> paramHashMap);

}
