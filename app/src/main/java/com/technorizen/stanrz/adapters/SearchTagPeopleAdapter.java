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
import com.technorizen.stanrz.databinding.TagSearchItemBinding;
import com.technorizen.stanrz.models.SuccessResGetUser;
import com.technorizen.stanrz.utility.TaggedUserId;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ravindra Birla on 06,July,2021
 */
public class SearchTagPeopleAdapter extends RecyclerView.Adapter<SearchTagPeopleAdapter.StoriesViewHolder> {

    private Context context;
    
    TagSearchItemBinding binding;

    private List<SuccessResGetUser.Result> usersList;

    private boolean from;

    private TaggedUserId taggedUserId;

    public SearchTagPeopleAdapter(Context context, List<SuccessResGetUser.Result> usersList, boolean from,TaggedUserId taggedUserId)
    {
      this.context = context;
      this.usersList = usersList;
      this.from = from;
      this.taggedUserId = taggedUserId;
    }
    
    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= TagSearchItemBinding.inflate(LayoutInflater.from(context));
        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {

        CircleImageView circleImageView = holder.itemView.findViewById(R.id.ivProfile);
        TextView tvUserName;
        RelativeLayout rlParent = holder.itemView.findViewById(R.id.rlParent);

        tvUserName = holder.itemView.findViewById(R.id.tvUserName);

        tvUserName.setText(usersList.get(position).getUsername());

        Glide
                .with(context)
                .load(usersList.get(position).getImage())
                .centerCrop()
                .into(circleImageView);

        rlParent.setOnClickListener(
                view -> {
                    taggedUserId.taggedId(position);
                }
        );
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(TagSearchItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
