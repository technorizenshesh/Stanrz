package com.technorizen.stanrz.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.SearchItemBinding;
import com.technorizen.stanrz.models.SuccessResGetUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ravindra Birla on 06,July,2021
 */
public class FollowersNFollowingAdapter extends RecyclerView.Adapter<FollowersNFollowingAdapter.StoriesViewHolder> {

    private Context context;

    SearchItemBinding binding;

    private List<SuccessResGetUser.Result> usersList;

    public FollowersNFollowingAdapter(Context context, List<SuccessResGetUser.Result> usersList)
    {
      this.context = context;
      this.usersList = usersList;
    }

    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= SearchItemBinding.inflate(LayoutInflater.from(context));
        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {

        CircleImageView circleImageView = holder.itemView.findViewById(R.id.ivProfile);
        TextView tvUserName,tvvFullName;

        RelativeLayout rlParent = holder.itemView.findViewById(R.id.rlParent);

        tvUserName = holder.itemView.findViewById(R.id.tvUserName);
        tvvFullName = holder.itemView.findViewById(R.id.tvFullName);

        tvUserName.setText(usersList.get(position).getUsername());
        tvvFullName.setText(usersList.get(position).getFullname());

        Glide
                .with(context)
                .load(usersList.get(position).getImage())
                .centerCrop()
                .into(circleImageView);

        rlParent.setOnClickListener(v ->
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("otherUser",usersList.get(position).getId());
                    Navigation.findNavController(v).navigate(R.id.action_searchUsersFragment_to_otherUserDetailFragment,bundle);
                }
                );

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(SearchItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
