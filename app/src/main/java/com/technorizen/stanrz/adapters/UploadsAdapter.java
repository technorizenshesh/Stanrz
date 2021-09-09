package com.technorizen.stanrz.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.UploadsItemBinding;
import com.technorizen.stanrz.databinding.UploadsItemBinding;
import com.technorizen.stanrz.models.SuccessResGetUploads;

import java.util.ArrayList;

/**
 * Created by Ravindra Birla on 06,July,2021
 */
public class UploadsAdapter extends RecyclerView.Adapter<UploadsAdapter.StoriesViewHolder> {

    private Context context;
    
    UploadsItemBinding binding;

    private ArrayList<SuccessResGetUploads.Result> uploadedPostList;

    private boolean from ;
    
    public UploadsAdapter(Context context,ArrayList<SuccessResGetUploads.Result> uploadedPostList,boolean from )
    {
      this.context = context;
      this.uploadedPostList=uploadedPostList;
      this.from = from;
    }
    
    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= UploadsItemBinding.inflate(LayoutInflater.from(context));
        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {
        ImageView ivPhoto = holder.itemView.findViewById(R.id.ivPhoto);
        Glide
                .with(context)
                .load(uploadedPostList.get(position).getUserPost().get(0).getImage())
                .centerCrop()
                .into(ivPhoto);

        ivPhoto.setOnClickListener(v ->
                {

                    if(from)
                    {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("key", uploadedPostList);
                        bundle.putString("pos",position+"");
                        Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_postsFragment,bundle);

                    }
                    else
                    {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("key", uploadedPostList);
                        bundle.putString("pos",position+"");
                        Navigation.findNavController(v).navigate(R.id.action_otherUserDetailFragment_to_postsFragment,bundle);

                    }

                }
                );

    }

    @Override
    public int getItemCount() {
        return uploadedPostList.size();
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(UploadsItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
