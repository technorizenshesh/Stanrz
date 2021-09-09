package com.technorizen.stanrz.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.ItemUploadingBinding;
import com.technorizen.stanrz.databinding.ItemUploadingBinding;
import com.technorizen.stanrz.databinding.ItemUploadingBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ravindra Birla on 06,July,2021
 */
public class UploadPostsAdapter extends RecyclerView.Adapter<UploadPostsAdapter.StoriesViewHolder> {

    private Context context;
    
    ItemUploadingBinding binding;

    private ArrayList<String> images_videos;
    
    public UploadPostsAdapter(Context context,ArrayList<String> images_videos)
    {
      this.context = context;
      this.images_videos = images_videos;
    }
    
    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= ItemUploadingBinding.inflate(LayoutInflater.from(context));
        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {
        Glide
                .with(context)
                .load(images_videos.get(position))
                .centerCrop()
                .placeholder(R.drawable.ic_user)
                .into(binding.ivUploadinPhoto);

    }

    @Override
    public int getItemCount() {
        return images_videos.size();
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(ItemUploadingBinding itemView) {
            super(itemView.getRoot());
        }
    }



}
