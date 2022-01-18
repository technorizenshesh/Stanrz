package com.technorizen.stanrz.utility;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.activites.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Ravindra Birla on 18,August,2021
 */
public class StanrzNotification extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private String notificationType, title, meetupId, messageId;
    private String message;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "payload:" + remoteMessage.getData());

            Map<String,String> map = remoteMessage.getData();

            try {
                sendNotification("","",map);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.d(TAG, "onMessageReceived for FCM");

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "c: " + remoteMessage.getData());
            try {
                sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), remoteMessage.getData());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendNotification(String message, String title,Map<String,String> map) throws JSONException {

        JSONObject jsonObject = null;
        jsonObject = new JSONObject(map);
        String key = jsonObject.getString("key");
        Intent intent = new Intent();
        String key1 = jsonObject.getString("message");

        if(key.equalsIgnoreCase("Follow"))
        {
            String result = jsonObject.getString("result");
            intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("notification","notification");
            intent.putExtra("key","notification");
            intent.putExtra("chat","test");
        }  else if(key.equalsIgnoreCase("Like"))
        {
            String result = jsonObject.getString("result");
            intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("notification","notification");
            intent.putExtra("key","notification");
            intent.putExtra("chat","test");

            if(Util.appInForeground(this))

            {
                Intent intent1 = new Intent("filter_string");
                intent.putExtra("key", "My Data");
                // put your all data using put extra
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
            }

        } else if(key.equalsIgnoreCase("Comment"))
        {
            intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("notification","notification");
            if(Util.appInForeground(this))

            {
                Intent intent1 = new Intent("filter_string");
                intent.putExtra("key", "My Data");
                // put your all data using put extra
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
            }

        }else if(key.equalsIgnoreCase("Super Like"))
        {
            intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("notification","notification");
            if(Util.appInForeground(this))

            {
                Intent intent1 = new Intent("filter_string");
                intent.putExtra("key", "My Data");
                // put your all data using put extra
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
            }

        }else if(key.equalsIgnoreCase("Chat"))
        {
            String userId = jsonObject.getString("userid");
            intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("notification","chat");
            intent.putExtra("userId",userId);
            if(Util.appInForeground(this))

            {
                Intent intent1 = new Intent("filter_string");
                intent.putExtra("key", "My Data");
                // put your all data using put extra
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
            }
        }


       /* if (jsonObject.has("data")) {
            JSONObject object = new JSONObject(map);
            Log.i("onMessageReceived: ", object.toString());
            title = object.optString("title");
            notificationType = object.optString("type");
            message = object.optString("message");
        }*/

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_logo)
                        .setContentTitle(title)
                        .setContentText(key1)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
