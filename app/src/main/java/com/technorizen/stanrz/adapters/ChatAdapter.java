package com.technorizen.stanrz.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.activites.PlayVideoActivity;
import com.technorizen.stanrz.activites.StoryDetailActivity;
import com.technorizen.stanrz.databinding.AdapterChatBinding;
import com.technorizen.stanrz.models.SuccessResGetChat;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Ravindra Birla on 18,March,2021
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

   AdapterChatBinding binding;

   private Context context;
   private List<SuccessResGetChat.Result> chatList = new LinkedList<>();
   String myId;

 public ChatAdapter(Context context, List<SuccessResGetChat.Result> chatList, String myId)
 {
     this.context = context;
     this.chatList = chatList;
     this.myId = myId;
 }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_chat, parent, false);
        return new ChatViewHolder(binding);
    }
    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ImageView ivLeft,ivRight,ivLeftVideoPlay,ivRightVideoPlay;
        TextView leftTime1,rightTime1,tvleftTime,tvrightTime,tvLeftMessage,tvRightMessage;
        ivLeft = holder.itemView.findViewById(R.id.left_chat_image);
        leftTime1 = holder.itemView.findViewById(R.id.chat_left_msg_text_view1);
        ivRight = holder.itemView.findViewById(R.id.right_chat_image);
        rightTime1 = holder.itemView.findViewById(R.id.tv_time_right1);
        tvleftTime = holder.itemView.findViewById(R.id.tv_time_left);
        tvLeftMessage = holder.itemView.findViewById(R.id.chat_left_msg_text_view);
        tvrightTime = holder.itemView.findViewById(R.id.tv_time_right);
        tvRightMessage = holder.itemView.findViewById(R.id.chat_right_msg_text_view);

        ivLeftVideoPlay = holder.itemView.findViewById(R.id.ivLeftVideoPlay);

        ivRightVideoPlay = holder.itemView.findViewById(R.id.ivRightVideoPlay);

        if(myId.equalsIgnoreCase(chatList.get(position).getSenderId()))
     {
         binding.chatLeftMsgLayout.setVisibility(View.GONE);
         binding.chatRightMsgLayout.setVisibility(View.VISIBLE);
         tvrightTime.setText(chatList.get(position).getTimeAgo());
         tvRightMessage.setText(decodeEmoji(chatList.get(position).getChatMessage()));

         Glide.with(context)
                 .load(chatList.get(position).getImageVideo())
                 .into(ivRight);
        rightTime1.setText(chatList.get(position).getTimeAgo());

         if(chatList.get(position).getType().equalsIgnoreCase("image") || chatList.get(position).getType().equalsIgnoreCase("video"))
         {

             tvRightMessage.setVisibility(View.GONE);
             tvrightTime.setVisibility(View.GONE);
             ivRight.setVisibility(View.VISIBLE);
             rightTime1.setVisibility(View.VISIBLE);

             if(chatList.get(position).getType().equalsIgnoreCase("image"))
             {
                 ivRightVideoPlay.setVisibility(View.GONE);
             }
             else
             {
                 ivRightVideoPlay.setVisibility(View.VISIBLE);
             }

         }
         else
         {

             tvRightMessage.setVisibility(View.VISIBLE);
             tvrightTime.setVisibility(View.VISIBLE);
             ivRight.setVisibility(View.GONE);
             rightTime1.setVisibility(View.GONE);
             ivRightVideoPlay.setVisibility(View.GONE);
         }

     }
     else
     {
         binding.chatLeftMsgLayout.setVisibility(View.VISIBLE);
         binding.chatRightMsgLayout.setVisibility(View.GONE);
         tvleftTime.setText(chatList.get(position).getTimeAgo());
         tvLeftMessage.setText(decodeEmoji(chatList.get(position).getChatMessage()));

         Glide.with(context)
                 .load(chatList.get(position).getImageVideo())
                 .fitCenter()
                 .into(ivLeft);
         leftTime1.setText(chatList.get(position).getTimeAgo());

         if(chatList.get(position).getType().equalsIgnoreCase("image") || chatList.get(position).getType().equalsIgnoreCase("video"))
         {
             tvLeftMessage.setVisibility(View.GONE);
             tvleftTime.setVisibility(View.GONE);
             ivLeft.setVisibility(View.VISIBLE);
             leftTime1.setVisibility(View.VISIBLE);
             if(chatList.get(position).getType().equalsIgnoreCase("image"))
             {
                 ivLeftVideoPlay.setVisibility(View.GONE);
             }
             else
             {
                 ivLeftVideoPlay.setVisibility(View.VISIBLE);
             }
         }
         else
         {
             tvLeftMessage.setVisibility(View.VISIBLE);
             tvleftTime.setVisibility(View.VISIBLE);
             ivLeft.setVisibility(View.GONE);
             leftTime1.setVisibility(View.GONE);
             ivLeftVideoPlay.setVisibility(View.GONE);
         }
     }

     ivLeft.setOnClickListener(v ->
             {

                 if(chatList.get(position).getType().equalsIgnoreCase("video"))
                 {
                     Intent intent = new Intent(context, PlayVideoActivity.class);
                     intent.putExtra("uri",chatList.get(position).getImageVideo());
                     context.startActivity(intent);
                 }

             }
             );

        ivRight.setOnClickListener(v ->
                {

                    if(chatList.get(position).getType().equalsIgnoreCase("video"))
                    {
                        Intent intent = new Intent(context, PlayVideoActivity.class);
                        intent.putExtra("uri",chatList.get(position).getImageVideo());
                        context.startActivity(intent);
                    }
                }
        );



    }


    public static String decodeEmoji (String message) {
        String myString= null;
        try {
            return URLDecoder.decode(
                    message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return message;
        }
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder
    {

        public ChatViewHolder(AdapterChatBinding adapterChatBinding) {
            super(adapterChatBinding.getRoot());
        }
    }
}
