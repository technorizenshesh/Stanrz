package com.technorizen.stanrz.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.SearchItemBinding;
import com.technorizen.stanrz.databinding.TaggedItemBinding;
import com.technorizen.stanrz.models.SuccessResGetUser;
import com.technorizen.stanrz.utility.TaggedUserId;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ravindra Birla on 06,July,2021
 */
public class TaggedPeopleAdapter extends RecyclerView.Adapter<TaggedPeopleAdapter.StoriesViewHolder> {

    private Context context;

    TaggedItemBinding binding;

    private TaggedUserId taggedUserId;

    private List<SuccessResGetUser.Result> usersList;

    private boolean from;

    public TaggedPeopleAdapter(Context context, List<SuccessResGetUser.Result> usersList, boolean from,TaggedUserId taggedUserId)
    {
      this.context = context;
      this.usersList = usersList;
      this.from = from;
      this.taggedUserId = taggedUserId;
    }
    
    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= TaggedItemBinding.inflate(LayoutInflater.from(context));
        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {

        TextView tvUserName;
        ImageView ivCancel;

        tvUserName = holder.itemView.findViewById(R.id.tvUserName);
        ivCancel  = holder.itemView.findViewById(R.id.ivCancel);
        tvUserName.setText(usersList.get(position).getUsername());

        ivCancel.setOnClickListener(view ->
                {
                    taggedUserId.taggedId(position);
                }
                );
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(TaggedItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
