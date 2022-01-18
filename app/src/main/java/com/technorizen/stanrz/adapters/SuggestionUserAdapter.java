package com.technorizen.stanrz.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.SuggestionItemBinding;
import com.technorizen.stanrz.databinding.SuggestionItemBinding;
import com.technorizen.stanrz.models.SuccessResGetUser;
import com.technorizen.stanrz.utility.FollowNFollowingNRemove;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ravindra Birla on 06,July,2021
 */
public class SuggestionUserAdapter extends RecyclerView.Adapter<SuggestionUserAdapter.StoriesViewHolder> {

    private Context context;
    
    SuggestionItemBinding binding;

    private List<SuccessResGetUser.Result> usersList;

    private FollowNFollowingNRemove followNFollowingNRemove;

    public SuggestionUserAdapter(Context context, List<SuccessResGetUser.Result> usersList,FollowNFollowingNRemove followNFollowingNRemove)
    {
      this.context = context;
      this.usersList = usersList;
      this.followNFollowingNRemove = followNFollowingNRemove;
    }
    
    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= SuggestionItemBinding.inflate(LayoutInflater.from(context));
        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {

        CircleImageView circleImageView = holder.itemView.findViewById(R.id.ivProfile);

        AppCompatButton btnFollow = holder.itemView.findViewById(R.id.btnFollow);

        TextView tvUserName;

        tvUserName = holder.itemView.findViewById(R.id.tvUserName);

        tvUserName.setText(usersList.get(position).getUsername());

        Glide
                .with(context)
                .load(usersList.get(position).getImage())
                .centerCrop()
                .into(circleImageView);

        btnFollow.setOnClickListener(v ->
                {
                    followNFollowingNRemove.follow(usersList.get(position).getId());
                }
                );
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(SuggestionItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
