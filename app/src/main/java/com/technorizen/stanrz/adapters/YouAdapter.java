package com.technorizen.stanrz.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.YouItemBinding;
import com.technorizen.stanrz.databinding.YouItemBinding;
import com.technorizen.stanrz.models.SuccessResGetNotifications;

import java.util.ArrayList;
import java.util.LinkedList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ravindra Birla on 06,July,2021
 */
public class YouAdapter extends RecyclerView.Adapter<YouAdapter.StoriesViewHolder> {

    private Context context;
    
    YouItemBinding binding;

    private LinkedList<SuccessResGetNotifications.Result> notificationList;
    
    public YouAdapter(Context context,LinkedList<SuccessResGetNotifications.Result> notificationList)
    {
      this.context = context;
      this.notificationList = notificationList;
    }
    
    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= YouItemBinding.inflate(LayoutInflater.from(context));
        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {

        CircleImageView ivProfile = holder.itemView.findViewById(R.id.ivProfile);
        ImageView ivPost = holder.itemView.findViewById(R.id.iv_post);
        TextView tvContent = holder.itemView.findViewById(R.id.tvMessage);
        TextView tvTime = holder.itemView.findViewById(R.id.tvTimeAgo);

        if (notificationList.get(position).getKey().equalsIgnoreCase("Mentioned In Comment")
                || notificationList.get(position).getKey().equalsIgnoreCase("Comment")
        )
        {
            tvContent.setOnClickListener(view ->
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("postID",notificationList.get(position).getPostId());
                        Navigation.findNavController(view).navigate(R.id.action_navigation_notification_to_addCommentFragment,bundle);
                    }
                    );
        }


        ivPost.setOnClickListener(view ->
                {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("key", notificationList.get(position).getPostId());
                    Navigation.findNavController(view).navigate(R.id.action_navigation_notification_to_notificationPostFragment,bundle);

                }
                );

        ivProfile.setOnClickListener(view ->
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("otherUser", notificationList.get(position).getUserId());
                    Navigation.findNavController(view).navigate(R.id.action_navigation_notification_to_otherUserDetailFragment,bundle);

                }
        );

        if(notificationList.get(position).getKey().equalsIgnoreCase("Like")
                || notificationList.get(position).getKey().equalsIgnoreCase("Comment")
                || notificationList.get(position).getKey().equalsIgnoreCase("Super Like")
                || notificationList.get(position).getKey().equalsIgnoreCase("Tag Post")
                || notificationList.get(position).getKey().equalsIgnoreCase("Mentioned In Comment")
        )
        {
            ivPost.setVisibility(View.VISIBLE);

            Glide
                    .with(context)
                    .load(notificationList.get(position).getPostImage())
                    .centerCrop()
                    .placeholder(R.drawable.ic_lock11)
                    .into(ivPost);
        }
        else
        {

            ivPost.setVisibility(View.GONE);

        }

        tvTime.setText(notificationList.get(position).getTimeAgo());

        Glide.with(context)
                .load(notificationList.get(position).getUserImage())
                .centerCrop()
                .placeholder(R.drawable.ic_user)
                .into(ivProfile);

        tvContent.setText(notificationList.get(position).getMessage());

    }


    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(YouItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
