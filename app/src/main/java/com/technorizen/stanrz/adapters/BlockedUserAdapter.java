package com.technorizen.stanrz.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.BlockedItemBinding;
import com.technorizen.stanrz.databinding.BlockedItemBinding;
import com.technorizen.stanrz.models.SuccessResGetBlockedUser;
import com.technorizen.stanrz.models.SuccessResGetStanrzOf;
import com.technorizen.stanrz.utility.SubscriptionClick;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ravindra Birla on 06,July,2021
 */
public class BlockedUserAdapter extends RecyclerView.Adapter<BlockedUserAdapter.StoriesViewHolder> {

    private Context context;

    BlockedItemBinding binding;

    private ArrayList<SuccessResGetBlockedUser.Result> topStanrzList;

    private SubscriptionClick subscriptionClick;

    public BlockedUserAdapter(Context context, ArrayList<SuccessResGetBlockedUser.Result> topStanrzList,SubscriptionClick subscriptionClick)
    {
      this.context = context;
      this.topStanrzList=topStanrzList;
      this.subscriptionClick = subscriptionClick;
    }

    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= BlockedItemBinding.inflate(LayoutInflater.from(context));
        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {

        CircleImageView circleImageView = holder.itemView.findViewById(R.id.iv_profile);

        TextView textName = holder.itemView.findViewById(R.id.tv_name);

        AppCompatButton btnUnblock = holder.itemView.findViewById(R.id.btnUnblock);

        Glide
                .with(context)
                .load(topStanrzList.get(position).getUserImage())
                .centerCrop()
                .into(circleImageView);

        textName.setText(topStanrzList.get(position).getUserName());

        btnUnblock.setOnClickListener(view ->
                {
                    subscriptionClick.superLikeClick(view,0,topStanrzList.get(position).getBlockUserId());
                }
                );

    }

    @Override
    public int getItemCount() {
        return topStanrzList.size();
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(BlockedItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
