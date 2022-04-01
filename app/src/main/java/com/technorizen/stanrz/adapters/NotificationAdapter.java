package com.technorizen.stanrz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.YouItemBinding;
import com.technorizen.stanrz.models.SuccessResGetNotifications;
import com.technorizen.stanrz.models.SuccessResGetUserActivity;

import java.util.ArrayList;
import java.util.LinkedList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ravindra Birla on 06,July,2021
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.StoriesViewHolder> {

    private Context context;

    YouItemBinding binding;

    private ArrayList<SuccessResGetUserActivity.Result> notificationList;

    public NotificationAdapter(Context context, ArrayList<SuccessResGetUserActivity.Result> notificationList)
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

        if(notificationList.get(position).getActivityType().equalsIgnoreCase("Like") || notificationList.get(position).getActivityType().equalsIgnoreCase("Comment") || notificationList.get(position).getActivityType().equalsIgnoreCase("Add Superlikes"))
        {

            ivPost.setVisibility(View.VISIBLE);

            Glide
                    .with(context)
                    .load(notificationList.get(position).getImage())
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

        tvContent.setText(notificationList.get(position).getActivity());
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
